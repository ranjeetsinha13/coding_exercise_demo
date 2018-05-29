package com.rs.mylivn.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.rs.mylivn.R
import com.rs.mylivn.entity.ImageItem
import com.rs.mylivn.toast
import com.rs.mylivn.viewmodels.ImagesViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.single_item_view.view.*
import org.jetbrains.anko.*
import java.util.*

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


class ImagesRecyclerViewAdapter(private val context: Context,
                                private val images: MutableList<ImageItem>,
                                private val viewModel: ImagesViewModel) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), Transformation, View.OnDragListener {

    override fun onDrag(v: View?, event: DragEvent?): Boolean {
        return true
    }


    override fun key(): String {
        return "imageKey"
    }

    companion object {
        var screenWidth: Int = 0
        var screenHeight: Int = 0

    }

    override fun transform(source: Bitmap): Bitmap {
        val targetWidth: Int
        val targetHeight: Int
        val aspectRatio: Double
        if (source.width > source.height) {
            targetWidth = screenWidth / 3
            aspectRatio = source.height.toDouble() / source.width.toDouble()
            targetHeight = (targetWidth * aspectRatio).toInt();
        } else {
            targetHeight = screenHeight / 4
            aspectRatio = source.width.toDouble() / source.height.toDouble()
            targetWidth = (targetHeight * aspectRatio).toInt()
        }

        val result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false)
        if (result != source) {
            source.recycle()
        }
        return result
    }


    val TAG = ImagesRecyclerViewAdapter::class.java.name

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        screenHeight = context.windowManager.defaultDisplay.height
        screenWidth = context.windowManager.defaultDisplay.width
        val view = LayoutInflater.from(context).inflate(R.layout.single_item_view, parent, false)
        view.setOnDragListener(this)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return images.size + 1
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //TODO(RS): The images do not fit well when the aspect ratio is not proper; apply transform
        if (position < images.size) {
            Picasso.get().load(images[position].imageURL).transform(this)
                    .into(holder.itemView.image_icon)
        }
        if (position == images.size) {
            Picasso.get().load(R.drawable.ic_add).transform(this).into(holder.itemView.image_icon)
            holder.itemView.cancel_button.visibility = View.GONE
            holder.itemView.setBackgroundColor(context.getColor(R.color.colorPrimary))
        }

        holder.itemView.setOnClickListener {
            Log.d(TAG, position.toString() + " clicked.....")
            if (position < images.size) {
                context.toast(String.format(context.getString(R.string.item_select_message), images[position].uuid))
            }
            if (position == images.size) {
                //Add logic to Add images
                var input: EditText? = null
                context.alert {
                    title = context.getString(R.string.add_image)

                    customView {
                        input =
                                editText {
                                    hint = context.getString(R.string.add_image_hint)
                                }
                    }
                    positiveButton(context.getString(R.string.add_image)) {
                        Log.d(TAG, " iamge url to add is " + input!!.text.toString())
                        addImage(input!!.text.toString())

                    }
                    negativeButton(context.getString(R.string.cancel)) {
                        it.dismiss()
                    }
                }.show()
            }
        }

        holder.itemView.cancel_button.setOnClickListener {
            Log.d(TAG, " delete item $position")
            //Create AlertDialog and delete images
            context.alert {
                title = context.getString(R.string.delete_image)
                positiveButton(context.getString(R.string.yes)) {
                    viewModel.deleteImage(images[position].uuid)
                    images.removeAt(position)
                    this@ImagesRecyclerViewAdapter.notifyItemRemoved(position)
                }
                negativeButton(context.getString(R.string.no)) { it.dismiss() }
            }.show()

        }

    }

    private fun addImage(imageUrl: String) {
        viewModel.addImage(imageUrl).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    images.add(it)
                    this@ImagesRecyclerViewAdapter.notifyItemInserted(images.size - 1)

                }, { throwable: Throwable? ->
                    throwable?.printStackTrace()
                    context.toast(context.getString(R.string.error_with_fetching_data_message))

                })
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {

        if (fromPosition < images.size && toPosition < images.size) {
            Collections.swap(images, fromPosition, toPosition)
            Log.d(TAG, "after swap images " + images.toString())
            doAsync {
                viewModel.swapImages(fromPosition, toPosition)
            }

        }
        notifyItemMoved(fromPosition, toPosition)

    }
}

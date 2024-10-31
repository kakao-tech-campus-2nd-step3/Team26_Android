package org.ktc2.cokaen.wouldyouin.core

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter(
    value = ["imageUrl", "imagePlaceHolder"],
    requireAll = false
)
fun setImageUrl(imageView: ImageView, url: String?, placeHolder: Drawable?) {
    val ph = placeHolder ?: ContextCompat.getDrawable(imageView.context, R.drawable.default_image)

    Glide.with(imageView.context)
        .load(url)
        .placeholder(ph)
        .error(ph)
        .into(imageView)
}
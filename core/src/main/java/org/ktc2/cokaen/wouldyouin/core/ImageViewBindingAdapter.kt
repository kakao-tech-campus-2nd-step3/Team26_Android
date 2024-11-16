package org.ktc2.cokaen.wouldyouin.core

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter(
    value = ["imageUrl", "imagePlaceHolder"],
    requireAll = false
)
fun setImageUrl(imageView: ImageView, path: String?, placeHolder: Drawable?) {
    val ph = placeHolder ?: ContextCompat.getDrawable(imageView.context, R.drawable.default_image)

    // path가 null이거나 빈 문자열이면 플레이스홀더 표시
    if (path.isNullOrEmpty()) {
        imageView.setImageDrawable(ph)
        return
    }


    Glide.with(imageView.context)
        .load(path)
        .placeholder(ph)
        .error(ph)
        .into(imageView)
}

@BindingAdapter(
    value = ["circleImageUrl", "imagePlaceHolder"],
    requireAll = false
)
fun circleImageUrl(imageView: ImageView, path: String?, placeHolder: Drawable?) {
    val ph = placeHolder ?: ContextCompat.getDrawable(imageView.context, R.drawable.default_image)

    // path가 null이거나 빈 문자열이면 플레이스홀더 표시
    if (path.isNullOrEmpty()) {
        imageView.setImageDrawable(ph)
        return
    }

    Glide.with(imageView.context)
        .load(path)
        .placeholder(ph)
        .circleCrop()
        .error(ph)
        .into(imageView)
}

@BindingAdapter(
    value = ["adImageUrl", "imagePlaceHolder"],
    requireAll = false
)
fun setAdImageUrl(imageView: ImageView, path: String?, placeHolder: Drawable?) {
    val ph = placeHolder ?: ContextCompat.getDrawable(imageView.context, R.drawable.default_image)

    // path가 null이거나 빈 문자열이면 플레이스홀더 표시
    if (path.isNullOrEmpty()) {
        imageView.setImageDrawable(ph)
        return
    }


    Glide.with(imageView.context)
        .load("https://wouldyouin.store/api/images/${path}")
        .placeholder(ph)
        .error(ph)
        .into(imageView)
}
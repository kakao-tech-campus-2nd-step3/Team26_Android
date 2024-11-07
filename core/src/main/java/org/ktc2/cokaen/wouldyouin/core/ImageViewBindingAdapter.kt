package org.ktc2.cokaen.wouldyouin.core

import android.graphics.drawable.Drawable
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

    // 수정해야할수도...
    val fullUrl = "http://52.78.71.136/api/$path"

    Glide.with(imageView.context)
        .load(fullUrl)
        .placeholder(ph)
        .error(ph)
        .into(imageView)
}
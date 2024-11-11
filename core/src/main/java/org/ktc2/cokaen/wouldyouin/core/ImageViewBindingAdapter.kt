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

    val tempUrl = "http://52.78.71.136/api/images/curation/1ac60366aa6f4de6b903b5a2e2721244.png"
    // path가 null이거나 빈 문자열이면 플레이스홀더 표시
    if (path.isNullOrEmpty()) {
        imageView.setImageDrawable(ph)
        return
    }

    Log.d("Adapter called", "Hi")

    Glide.with(imageView.context)
        .load(tempUrl)
        .placeholder(ph)
        .error(ph)
        .into(imageView)
}
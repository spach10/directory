package org.jdc.template.ui.binding

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.jdc.template.model.db.main.type.IndividualAffiliationType
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime

object CustomBinders {

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("affiliate")
    fun setAffiliate(view: TextView, affiliationType: IndividualAffiliationType?) {
        affiliationType?.let {
            var affiliationString = ""
            val affiliationArray = it.toString().split('_')
            if (affiliationArray.size > 1) {
                for (word in affiliationArray)
                    affiliationString += " $word"
            } else {
                affiliationString = affiliationArray[0]
            }

            view.text = "Affiliation: $affiliationString"
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("forceSensitive")
    fun setForceSensitive(view: TextView, bool: Boolean?) {
        bool?.let {
            val boolString = if (it) "YES" else "NO"
            view.text = "Force sensitive: $boolString"
        }
    }

    @JvmStatic
    @BindingAdapter("profileImage")
    fun setProfileImage(view: ImageView, url: String?) {
        url?.let {
            Glide.with(view.context)
                    .load(url).apply(RequestOptions().circleCrop())
                    .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("textDate")
    fun setTextDate(view: TextView, date: LocalDate?) {
        var text = ""
        date?.let {
            val millis = OffsetDateTime.now().with(date).toInstant().toEpochMilli()
            text = DateUtils.formatDateTime(view.context, millis, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        }

        view.text = text
    }

    @JvmStatic
    @BindingAdapter("textTime")
    fun setTextTime(view: TextView, time: LocalTime?) {
        var text = ""
        time?.let {
            val millis = OffsetDateTime.now().with(time).toInstant().toEpochMilli()
            text = DateUtils.formatDateTime(view.context, millis, DateUtils.FORMAT_SHOW_TIME)
        }
        view.text = text
    }

    @JvmStatic
    @BindingAdapter("buildTimeText")
    fun setAppBuildTimeText(view: TextView, millis: Long) {
        val dateText = DateUtils.formatDateTime(view.context, millis, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_YEAR)
        view.text = dateText
    }
}

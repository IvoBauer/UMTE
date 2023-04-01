package cz.uhk.umte.data.enums

import androidx.annotation.StringRes
import cz.uhk.umte.R

enum class Gender(@StringRes val nameRes: Int) {
    Man(R.string.form_screen_select_gender_man),
    Woman(R.string.form_screen_select_gender_woman),
    Undefined(R.string.form_screen_select_gender_undefined),
}
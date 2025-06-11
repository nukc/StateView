package com.github.nukc.sample

import android.os.Bundle
import android.view.View
import com.github.nukc.stateview.animations.SlideAnimatorProvider

class WrapLinearLayoutChildActivity : BaseActivity() {
    override fun injectTarget(): View {
        return findViewById(R.id.tv_content)
    }

    override fun setContentView(): Int {
        return R.layout.activity_wrap_linearlayout_child
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAnimator(SlideAnimatorProvider())
    }
}

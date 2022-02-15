package com.github.nukc.stateview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingParent
import androidx.core.view.ScrollingView
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentContainerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * StateView is an invisible, zero-sized View that can be used
 * to lazily inflate loadingView/emptyView/retryView/anyView at runtime.
 *
 * @author Nukc.
 */
class StateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val views = SparseArray<View>(3)

    @LayoutRes
    var emptyResource = 0

    @LayoutRes
    var retryResource = 0

    @LayoutRes
    var loadingResource = 0

    var inflater: LayoutInflater? = null
    var onRetryClickListener: OnRetryClickListener? = null
    var onInflateListener: OnInflateListener? = null

    var animatorProvider: AnimatorProvider? = null
        set(value) {
            field = value
            for (i in 0 until views.size()) {
                reset(views.valueAt(i))
            }
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StateView)
        emptyResource = a.getResourceId(R.styleable.StateView_emptyResource, 0)
        retryResource = a.getResourceId(R.styleable.StateView_retryResource, 0)
        loadingResource = a.getResourceId(R.styleable.StateView_loadingResource, 0)
        a.recycle()

        if (emptyResource == 0) {
            emptyResource = R.layout.base_empty
        }
        if (retryResource == 0) {
            retryResource = R.layout.base_retry
        }
        if (loadingResource == 0) {
            loadingResource = R.layout.base_loading
        }

        visibility = GONE
        setWillNotDraw(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(0, 0)
    }

    @SuppressLint("MissingSuperCall")
    override fun draw(canvas: Canvas?) {
    }

    override fun dispatchDraw(canvas: Canvas?) {}

    override fun setVisibility(visibility: Int) {
        for (i in 0 until views.size()) {
            setVisibility(views.valueAt(i), visibility)
        }
    }

    private fun setVisibility(view: View?, visibility: Int) {
        if (view != null && visibility != view.visibility) {
            if (animatorProvider != null) {
                startAnimation(view)
            } else {
                view.visibility = visibility
            }
        }
    }

    fun setView(viewType: Int, view: View) {
        views.put(viewType, view)
    }

    fun showContent() {
        visibility = GONE
    }

    fun showEmpty() = showView(emptyResource)

    fun showRetry() = showView(retryResource)

    fun showLoading() = showView(loadingResource)

    /**
     * show the viewType view
     */
    fun show(viewType: Int) = showView(viewType)

    /**
     * show the state view
     */
    private fun showView(@LayoutRes layoutResource: Int): View {
        var view = views[layoutResource]
        if (view == null) {
            view = inflate(layoutResource)
            views.put(layoutResource, view)
        } else if ((parent as ViewGroup).indexOfChild(view) == NO_ID) {
            addToParent(layoutResource, parent as ViewGroup, view)
        }
        setVisibility(view, VISIBLE)
        hideViews(view)
        return view
    }

    /**
     * hide other views after show view
     */
    private fun hideViews(showView: View) {
        for (i in 0 until views.size()) {
            val view = views.valueAt(i)
            if (view == showView) {
                continue
            }
            setVisibility(view, GONE)
        }
    }

    private fun startAnimation(view: View) {
        val toShow = view.visibility == GONE
        (if (toShow) animatorProvider!!.showAnimation(view)
        else animatorProvider!!.hideAnimation(view))?.apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    if (!toShow) {
                        view.visibility = GONE
                    }
                }

                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    if (toShow) {
                        view.visibility = VISIBLE
                    }
                }
            })
            start()
        } ?: let {
            view.visibility = if (toShow) VISIBLE else GONE
        }
    }

    /**
     * reset view's property
     * 不然多次设置 [animatorProvider] 后视图动画会混乱
     */
    private fun reset(view: View?) {
        if (view != null) {
            view.translationX = 0f
            view.translationY = 0f
            view.alpha = 1f
            view.rotation = 0f
            view.scaleX = 1f
            view.scaleY = 1f
        }
    }

    private fun inflate(@LayoutRes layoutResource: Int): View {
        val viewParent = parent
        return if (viewParent is ViewGroup) {
            if (layoutResource != 0) {
                val factory: LayoutInflater = inflater ?: LayoutInflater.from(context)
                val view = factory.inflate(layoutResource, viewParent, false)
                addToParent(layoutResource, viewParent, view)
            } else {
                throw IllegalArgumentException("StateView must have a valid layoutResource")
            }
        } else {
            throw IllegalStateException("StateView must have a non-null ViewGroup viewParent")
        }
    }

    private fun addToParent(
        @LayoutRes layoutResource: Int,
        viewParent: ViewGroup,
        view: View
    ): View {
        val index = viewParent.indexOfChild(this)
        // 防止还能触摸底下的 View
        view.isClickable = true
        // 先不显示
        view.visibility = GONE
        ViewCompat.setZ(view, ViewCompat.getZ(this))
        if (layoutParams != null) {
            if (viewParent is RelativeLayout) {
                val lp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    RelativeLayout.LayoutParams(layoutParams as RelativeLayout.LayoutParams)
                } else {
                    RelativeLayout.LayoutParams(layoutParams)
                }
                viewParent.addView(view, index, lp)
            } else if (Injector.constraintLayoutAvailable && viewParent is ConstraintLayout) {
                val source = layoutParams as ConstraintLayout.LayoutParams
                val lp = ConstraintLayout.LayoutParams(layoutParams as ViewGroup.LayoutParams)
                lp.leftToLeft = source.leftToLeft
                lp.rightToRight = source.rightToRight
                lp.topToTop = source.topToTop
                lp.bottomToBottom = source.bottomToBottom
                viewParent.addView(view, index, lp)
            } else {
                viewParent.addView(view, index, layoutParams)
            }
        } else {
            viewParent.addView(view, index)
        }
        if (layoutResource == retryResource) {
            view.setOnClickListener {
                onRetryClickListener?.let {
                    showLoading()
                    view.postDelayed({
                        it.onRetryClick()
                    }, 400)
                }
            }
        }

        onInflateListener?.onInflate(layoutResource, view)
        return view
    }

    /**
     * Listener used to receive a notification after the RetryView is clicked.
     *
     * @see onRetryClickListener
     */
    interface OnRetryClickListener {
        fun onRetryClick()
    }

    /**
     * Listener used to receive a notification after a StateView has successfully
     * inflated its layout resource.
     *
     * @see onInflateListener
     */
    interface OnInflateListener {
        /**
         * @param layoutResource Equivalent viewType, the [view] of key
         * @param view The inflated View.
         */
        fun onInflate(@LayoutRes layoutResource: Int, view: View)
    }

    companion object {
        internal const val TAG = "StateView"

        @JvmStatic
        fun inject(activity: Activity): StateView {
            return inject(activity.window.decorView.findViewById<View>(android.R.id.content))
        }

        @JvmStatic
        fun inject(view: View): StateView {
            return when (view) {
                is ViewGroup -> inject(view)
                else -> wrap(view)
            }
        }

        @JvmStatic
        fun inject(viewGroup: ViewGroup): StateView {
            if (viewGroup is LinearLayout || viewGroup is ScrollView || viewGroup is AdapterView<*> ||
                (viewGroup is ScrollingView && viewGroup is NestedScrollingChild) ||
                (viewGroup is NestedScrollingParent && viewGroup is NestedScrollingChild)
            ) {
                return if (viewGroup.parent is ViewGroup && viewGroup.parent !is FragmentContainerView) {
                    wrap(viewGroup)
                } else {
                    Injector.wrapChild(viewGroup)
                }
            }

            // match the viewGroup
            val stateView = StateView(viewGroup.context)
            viewGroup.addView(
                stateView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            return stateView
        }

        @JvmStatic
        fun wrap(view: View): StateView {
            val parent = view.parent
            if (parent is ViewGroup) {
                if (Injector.constraintLayoutAvailable && parent is ConstraintLayout) {
                    return Injector.matchViewIfParentIsConstraintLayout(parent, view)
                }
                if (parent is RelativeLayout) {
                    return Injector.matchViewIfParentIsRelativeLayout(parent, view)
                }

                // will increase the layout level
                parent.removeView(view)
                val wrap = FrameLayout(view.context)
                parent.addView(wrap, view.layoutParams)
                wrap.addView(
                    view,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                val stateView = StateView(view.context)
                wrap.addView(
                    stateView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                Injector.setStateListAnimator(stateView, view)

                // special for SwipeRefreshLayout
                if (Injector.swipeRefreshLayoutAvailable && parent is SwipeRefreshLayout) {
                    Injector.injectIntoSwipeRefreshLayout(parent)
                }

                return stateView
            }
            throw ClassCastException("view.getParent() must be ViewGroup")
        }

    }
}

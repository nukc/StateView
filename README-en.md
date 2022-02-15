# StateView

[![Build Status](https://travis-ci.org/nukc/StateView.svg?branch=master)](https://travis-ci.org/nukc/StateView)
[![](https://jitpack.io/v/nukc/StateView.svg)](https://jitpack.io/#nukc/StateView)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-StateView-green.svg?style=true)](https://android-arsenal.com/details/1/4255)

[中文](https://github.com/nukc/StateView/blob/master/README-zh.md)

StateView is an invisible, zero-sized View that can be used to lazily inflate loadingView/emptyView/retryView/anyView at runtime.

<img src="https://raw.githubusercontent.com/nukc/stateview/master/art/custom.gif"><img width="200"><img src="https://raw.githubusercontent.com/nukc/stateview/master/art/animations.gif">

## Installation

> JitPack

Step 1. Add the JitPack repository to your build file
```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
Step 2. Add the dependency
```groovy
	dependencies {
	    implementation 'com.github.nukc:StateView:v3.0.2'
	}
```

> JCenter: will no available after 2022-02-01
```groovy
   // andoridx, kotlin version, recommend
   implementation 'com.github.nukc.stateview:kotlin:2.2.0'

   // support library, java version
   compile 'com.github.nukc.stateview:library:1.5.4'

   // animator providers
   compile 'com.github.nukc.stateview:animations:1.0.2'
```

##Usage

Can be directly used in java.

```java
    mStateView = StateView.inject(Activity activity);
```

```java
    mStateView = StateView.inject(View view);
```

```java
    mStateView = StateView.inject(ViewGroup parent);
```

Or include the StateView widget in your layout.

```xml

    <com.github.nukc.stateview.StateView
        android:id="@+id/stateView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

```

To switch the state view:

- ```mStateView.showEmpty();```
- ```mStateView.showLoading();```
- ```mStateView.showRetry();```
- ```mStateView.showContent();```

To listen the retry click:

```java
    mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
        @Override
        public void onRetryClick() {
            //do something, no need to call showLoading()
        }
    });
```

To customize view:

- Global settings way: create a new layout (layout's name must be ```base_empty```/```base_retry```/```base_loading```).

- Single page setting: create a new layout whit different name, and set resource in java.

```java
setEmptyResource(@LayoutRes int emptyResource)

setRetryResource(@LayoutRes int retryResource)

setLoadingResource(@LayoutRes int loadingResource)

// v2.1
setEmptyView(View view)
setRetryView(View view)
setLoadingView(View view)

// v3.0.0
setView(viewType: Int, view: View)

// v3.0.0
setView(viewType: Int, view: View)
// eg: set empty view
setView(mStateView.getEmptyResource(), emptyView)
// set any view
setView(1, view)
// show view
show(viewType: Int)
```

use setOnInflateListener to set message, like AnimatorActivity.java#L28


## Custom Attribute

```xml
<resources>
    <declare-styleable name="StateView">
        <attr name="emptyResource" format="reference" />
        <attr name="retryResource" format="reference" />
        <attr name="loadingResource" format="reference" />
    </declare-styleable>
</resources>
```

## Animation

set:

```java
    // provider default is null, no animation
    // if need, set a AnimatorProvider
    setAnimatorProvider(AnimatorProvider provider)

```

animation can custom, can also compile ```animations```

```groovy
    compile 'com.github.nukc.stateview:animations:1.0.1'

```

```animations``` library has:

- ```FadeScaleAnimatorProvider```
- ```FlipAnimatorProvider```
- ```SlideAnimatorProvider```


if want a custom animation，implements ```AnimatorProvider```

```java
public class FadeScaleAnimatorProvider implements AnimatorProvider {

    @Override
    public Animator showAnimation(View view) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0.1f, 1f)
        );
        return set;
    }

    @Override
    public Animator hideAnimation(View view) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.1f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.1f)
        );
        return set;
    }
}

```


## License

    The MIT License (MIT)

    Copyright (c) 2016 Nukc

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
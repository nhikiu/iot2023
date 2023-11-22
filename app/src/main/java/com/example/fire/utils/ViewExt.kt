package com.example.fire.utils

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.fire.R
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

fun View.showKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.GONE
}


/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                EspressoIdlingResource.increment()
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                EspressoIdlingResource.decrement()
            }
        })
        show()
    }
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<SingleEvent<Any>>,
    timeLength: Int
) {
    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            when (it) {
                is String -> {
                    hideKeyboard()
                    showSnackbar(it, timeLength)
                }
                is Int -> {
                    hideKeyboard()
                    showSnackbar(this.context.getString(it), timeLength)
                }
                else -> {
                }
            }

        }
    })
}

fun View.showToast(
    lifecycleOwner: LifecycleOwner,
    ToastEvent: LiveData<SingleEvent<Any>>,
    timeLength: Int
) {

    ToastEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            when (it) {
                is String -> Toast.makeText(this.context, it, timeLength).show()
                is Int -> Toast.makeText(this.context, this.context.getString(it), timeLength).show()
                else -> {
                }
            }
        }
    })
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun ImageView.loadImage(@DrawableRes resId: Int) = Picasso.get().load(resId).into(this)
fun ImageView.loadImage(url: String) = Picasso.get().load(url).placeholder(R.drawable.ic_star).error(R.drawable.ic_star).into(this)

fun AppCompatTextView.setTextFutureExt(text: String) =
    setTextFuture(
        PrecomputedTextCompat.getTextFuture(
            text,
            TextViewCompat.getTextMetricsParams(this),
            null
        )
    )

fun AppCompatEditText.setTextFutureExt(text: String) =
    setText(
        PrecomputedTextCompat.create(text, TextViewCompat.getTextMetricsParams(this))
    )

fun AppCompatActivity.replaceFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    isAddToBackStack: Boolean = true,
) {
    try {
        val fragmentTransaction = supportFragmentManager.beginTransaction()



        if (isAddToBackStack)
            fragmentTransaction.addToBackStack(fragment::class.java.name)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.replace(containerId, fragment, fragment::class.java.name)
        fragmentTransaction.commit()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Fragment.replaceFragment(
    @IdRes containerId: Int, fragment: Fragment,
    isAddToBackStack: Boolean = true,
    addFromActivity: Boolean = false
) {
    try {
        val fm = if (addFromActivity) (activity as AppCompatActivity).supportFragmentManager else childFragmentManager
        val fragmentTransaction = fm.beginTransaction()


        if (isAddToBackStack)
            fragmentTransaction.addToBackStack(fragment::class.java.name)


        fragmentTransaction.replace(containerId, fragment, fragment::class.java.name)
        fragmentTransaction.commit()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Fragment.replaceFragment(
    activity: Activity,
    @IdRes containerId: Int, fragment: Fragment,
    isAddToBackStack: Boolean = true,
    addFromActivity: Boolean = false
) {
    try {
        val fm = if (addFromActivity) (activity as AppCompatActivity).supportFragmentManager else childFragmentManager
        val fragmentTransaction = fm.beginTransaction()


        if (isAddToBackStack)
            fragmentTransaction.addToBackStack(fragment::class.java.name)


        fragmentTransaction.replace(containerId, fragment, fragment::class.java.name)
        fragmentTransaction.commit()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Uri?.openInBrowser(context: Context) {
    this ?: return // Do nothing if uri is null

    val browserIntent = Intent(Intent.ACTION_VIEW, this)
    ContextCompat.startActivity(context, browserIntent, null)
}
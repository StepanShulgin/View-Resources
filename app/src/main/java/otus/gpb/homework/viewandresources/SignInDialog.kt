package otus.gpb.homework.viewandresources

import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.WindowManager
import android.view.autofill.AutofillManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.min

class SignInDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val content = layoutInflater.cloneInContext(builder.context)
            .inflate(R.layout.dialog_signin, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            content.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }
        val password = content.findViewById<TextInputEditText>(R.id.password_input)
        content.findViewById<TextInputLayout>(R.id.password_field).setEndIconOnClickListener {
            val selection = password.selectionEnd
            password.transformationMethod =
                if (password.transformationMethod is PasswordTransformationMethod) null
                else PasswordTransformationMethod.getInstance()
            password.setSelection(selection.coerceAtLeast(0))
        }
        content.findViewById<Button>(R.id.cancel_button).setOnClickListener { closeDemo() }
        content.findViewById<Button>(R.id.login_button).setOnClickListener { closeDemo() }
        return builder.setView(content).create()
    }

    private fun cancelAutofill() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.getSystemService(AutofillManager::class.java)?.cancel()
        }
    }

    private fun closeDemo() {
        cancelAutofill()
        dismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        cancelAutofill()
        super.onCancel(dialog)
    }

    override fun onStart() {
        super.onStart()
        (activity as? MainActivity)?.setLauncherVisible(false)
        val metrics = resources.displayMetrics
        val width = min(
            resources.getDimensionPixelSize(R.dimen.login_window_width),
            metrics.widthPixels - resources.getDimensionPixelSize(R.dimen.login_screen_margin) * 2,
        )
        dialog?.window?.apply {
            setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
            attributes = attributes.apply {
                y = -resources.getDimensionPixelSize(R.dimen.login_vertical_offset)
                dimAmount = 0f
            }
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            @Suppress("DEPRECATION")
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? MainActivity)?.setLauncherVisible(true)
    }

    companion object {
        const val TAG = "sign_in"
    }
}

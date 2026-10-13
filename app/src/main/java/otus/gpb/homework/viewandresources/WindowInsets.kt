package otus.gpb.homework.viewandresources

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

internal fun AppCompatActivity.applyContentInsets() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowCompat.getInsetsController(window, window.decorView).apply {
        isAppearanceLightStatusBars = resources.getBoolean(R.bool.light_system_bars)
        isAppearanceLightNavigationBars = resources.getBoolean(R.bool.light_system_bars)
    }
    val root = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
        val safe = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or
                WindowInsetsCompat.Type.displayCutout() or
                WindowInsetsCompat.Type.ime(),
        )
        view.setPadding(safe.left, safe.top, safe.right, safe.bottom)
        insets
    }
    ViewCompat.requestApplyInsets(root)
}

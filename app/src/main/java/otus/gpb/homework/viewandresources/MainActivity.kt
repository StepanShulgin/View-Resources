package otus.gpb.homework.viewandresources

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isInvisible
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applyContentInsets()
        findViewById<Button>(R.id.contacts_button).setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))
        }
        findViewById<Button>(R.id.cart_button).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        findViewById<Button>(R.id.signin_button).setOnClickListener { showLogin() }
        findViewById<MaterialToolbar>(R.id.toolbar).apply {
            setOnMenuItemClickListener {
                showLogin()
                true
            }
            setNavigationOnClickListener {
                PopupMenu(this@MainActivity, this).apply {
                    menu.add(R.string.open_contacts).setOnMenuItemClickListener {
                        startActivity(Intent(this@MainActivity, ContactsActivity::class.java))
                        true
                    }
                    menu.add(R.string.open_cart).setOnMenuItemClickListener {
                        startActivity(Intent(this@MainActivity, CartActivity::class.java))
                        true
                    }
                    menu.add(R.string.show_sign_in_dialog).setOnMenuItemClickListener {
                        showLogin()
                        true
                    }
                    show()
                }
            }
        }
        setLauncherVisible(supportFragmentManager.findFragmentByTag(SignInDialog.TAG) == null)
    }

    internal fun setLauncherVisible(visible: Boolean) {
        findViewById<View>(R.id.launcher_buttons).isInvisible = !visible
    }

    private fun showLogin() {
        if (!supportFragmentManager.isStateSaved &&
            supportFragmentManager.findFragmentByTag(SignInDialog.TAG) == null
        ) {
            SignInDialog().showNow(supportFragmentManager, SignInDialog.TAG)
        }
    }
}

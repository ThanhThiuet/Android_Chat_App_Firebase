package studymyselft.nguyenthanhthi.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private int SIGN_IN_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Chatting");

        handlingSignIn(); // xu ly viec sign in cua user
    }

    /**
     * override method onActivityResult() de show Toast len man hinh nham thong bao user sign in thanh cong hay khong
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Successfully signed in. Welcome!", Toast.LENGTH_SHORT).show();
                displayChatMessages();
            } else {
                Toast.makeText(this, "Sign in fail! Please try again later", Toast.LENGTH_SHORT).show();
                // close the app
                finish();
            }
        }
    }

    /**
     * override method onCreateOptionMenu() de khoi tao menu main_menu (sign out)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * override method onOptionsItemSelected() de xu ly su kien nhap vao item menu (sign out)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_SHORT).show();
                            // close activity
                            finish();
                        }
                    });
        }
        return true;
    }

    /**
     * xu ly viec sign in cua User
     */
    private void handlingSignIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // user has not sign in
            // start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            // User has already signed in
            // desplay welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                    Toast.LENGTH_SHORT).show();
            // load contents of chat room
            displayChatMessages();
        }
    }

    private void displayChatMessages() {

    }
}

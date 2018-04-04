package studymyselft.nguyenthanhthi.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
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
     * xu ly viec sign in cua User
     */
    private void handlingSignIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            //user has not sign in
            //start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            //User has already signed in
            //desplay welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                    Toast.LENGTH_SHORT)
                    .show();
            //load contents of chat room
            displayChatMessages();
        }
    }

    private void displayChatMessages() {

    }
}

package studymyselft.nguyenthanhthi.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Chatting");

        handlingSignIn(); // xu ly viec sign in cua user
        sendMessage(); // xu ly viec gui tin nhan cua user
    }

    /**
     * xu ly viec gui gui tin nhan cua user
     */
    private void sendMessage() {
        FloatingActionButton button = findViewById(R.id.floating_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = findViewById(R.id.text_input);

                // Read the input field and push a new instance of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()  // tu dong sinh key moi
                        .setValue(new ChatMessage(
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                input.getText().toString()
                        ));
                // clear the input
                input.setText("");
            }
        });
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
                finish(); // close the app
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
                            finish(); // close activity
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

    /**
     * xu ly viec gui tin nhan chat
     */
    private void displayChatMessages() {
        ListView listMessages = findViewById(R.id.list_messages);

        Query query = FirebaseDatabase.getInstance().getReference().child("");

        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.message)
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageUser = findViewById(R.id.message_user);
                TextView messageText = findViewById(R.id.message_text);
                TextView messageTime = findViewById(R.id.message_time);

                messageUser.setText(model.getMessageUser());
                messageText.setText(model.getMessageText());

                // format date before showing
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy (HH:mm:ss)");
                messageTime.setText(dateFormat.format(model.getMessageTime()));
            }
        };

        listMessages.setAdapter(adapter);
    }
}

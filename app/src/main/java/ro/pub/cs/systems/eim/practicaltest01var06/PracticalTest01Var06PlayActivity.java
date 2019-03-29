package ro.pub.cs.systems.eim.practicaltest01var06;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class PracticalTest01Var06PlayActivity extends AppCompatActivity {
    private int recvNumber = 0;
    private int score = 0;
    private int generatedNumber = 0;

    private Button generateButton;
    private Button checkButton;
    private Button backButton;
    private TextView guessTextView;
    private EditText scoreEditText;
    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private Random random = new Random();

    private IntentFilter intentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var06_play_activity);

        generateButton = findViewById(R.id.generate_button);
        checkButton = findViewById(R.id.check_button);
        backButton = findViewById(R.id.back_button);
        guessTextView = findViewById(R.id.guess_text_view);
        scoreEditText = findViewById(R.id.score_edit_text);

        generateButton.setOnClickListener(buttonClickListener);
        checkButton.setOnClickListener(buttonClickListener);
        backButton.setOnClickListener(buttonClickListener);

        Intent intent = getIntent();
        if (intent != null) {
            String number = intent.getStringExtra("ro.pub.cs.systems.eim.practicaltest01var06.NUMBER_KEY");
            if (number != null) {
               recvNumber = Integer.valueOf(number);
            } else {
                Toast.makeText(this, getResources().getString(R.string.number_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.generate_button) {
                generatedNumber = random.nextInt(9);
                guessTextView.setText(String.valueOf(generatedNumber));
            }

            if(view.getId() == R.id.check_button) {
                if(generatedNumber == recvNumber) {
                    score++;
                }
                scoreEditText.setText(String.valueOf(score));
                generatedNumber = -1;
            }

            if(view.getId() == R.id.back_button) {
                finish();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(Constants.GUESS_TEXT_VIEW, guessTextView.getText().toString());
        savedInstanceState.putString(Constants.SCORE_EDIT_TEXT, scoreEditText.getText().toString());
        savedInstanceState.putString(Constants.SCORE, String.valueOf(score));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getString(Constants.GUESS_TEXT_VIEW) != null) {
            guessTextView.setText(savedInstanceState.getString(Constants.GUESS_TEXT_VIEW));
        }

        if (savedInstanceState.getString(Constants.SCORE_EDIT_TEXT) != null) {
            scoreEditText.setText(savedInstanceState.getString(Constants.SCORE_EDIT_TEXT));
        }

        if (savedInstanceState.getString(Constants.SCORE) != null) {
            score = Integer.valueOf(savedInstanceState.getString(Constants.SCORE));
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Var06Service.class);
        stopService(intent);
        super.onDestroy();
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }
}

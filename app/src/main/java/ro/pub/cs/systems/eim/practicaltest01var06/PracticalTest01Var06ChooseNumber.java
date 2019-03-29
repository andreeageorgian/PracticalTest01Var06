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
import android.widget.Toast;
import java.util.Random;

public class PracticalTest01Var06ChooseNumber extends AppCompatActivity {
    private Button playButton;
    private EditText numberEditText;
    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private IntentFilter intentFilter = new IntentFilter();
    private Random random = new Random();
    private Boolean serviceStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var06_choose_number);

        playButton = findViewById(R.id.play_button);
        numberEditText = findViewById(R.id.number_edit_text);
        playButton.setOnClickListener(buttonClickListener);
    }

    private class ButtonClickListener implements  Button.OnClickListener{
        @Override
        public void onClick(View view) {
            String number = numberEditText.getText().toString();
            if (number.length() > 0) {
                Intent intent = new Intent("ro.pub.cs.systems.eim.practicaltest01var06.intent.action.PracticalTest01Var06PlayActivity");
                intent.putExtra("ro.pub.cs.systems.eim.practicaltest01var06.NUMBER_KEY", number);
                startActivityForResult(intent, Constants.NUMBER_REQUEST_CODE);
            } else {
                Toast.makeText(getApplication(), getResources().getString(R.string.number_error), Toast.LENGTH_LONG).show();
            }

            Intent intent_aux = new Intent(getApplicationContext(), PracticalTest01Var06Service.class);
            intentFilter.addAction(Constants.ACTION);
            intent_aux.putExtra("randomNumber", random.nextInt(9));
            getApplicationContext().startService(intent_aux);
            serviceStatus = Constants.SERVICE_STARTED;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Constants.NUMBER_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }
}

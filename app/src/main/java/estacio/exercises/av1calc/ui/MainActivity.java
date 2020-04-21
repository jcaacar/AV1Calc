package estacio.exercises.av1calc.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import estacio.exercises.av1calc.App;
import estacio.exercises.av1calc.R;
import estacio.exercises.av1calc.core.CalculatorExpression;

public class MainActivity extends AppCompatActivity {

    private TextView tvRPN;
    private TextView tvInput;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadViews();
        setListeners();
    }

    /* BUTTON'S EVENTS */
    public void btnOnClick(View view) {
        String value = ((Button)view).getText().toString();

        if(isInputEmpty()) {
            this.tvInput.setText(value);
        } else {
            this.tvInput.setText(this.tvInput.getText() + value);
        }
    }

    public void btnEqualsOnClick(View view) {
        try {
            if(!isInputEmpty()) {
                CalculatorExpression ce = new CalculatorExpression(this.tvInput.getText().toString());
                int result = (int)ce.execute();

                setResultView(String.valueOf(result));
                setRPNView(ce.getRPN());
            } else {
                showMessage(R.string.exception_expr_not_found);
            }
        } catch(Exception ex) {
            showMessage(ex.getMessage());
        }
    }

    public void btnBackspaceOnClick(View view) {
        if(!isInputEmpty()) {
            if(this.tvResult.getText().length() == 0) { // if not have result yet just remove last caracter from input
                String currInput = this.tvInput.getText().toString();
                String newInput = currInput.substring(0, currInput.length()-1);

                setInputView(newInput);
            } else { // only clean result and RPN labels
                setResultView("");
                setRPNView("");
                setInputView(this.tvInput.getText().toString()); // I don't know why, but it solved the problem
            }
        }
    }

    /* AUX */
    private void loadViews() {
        this.tvInput = (TextView)findViewById(R.id.tvInput);
        this.tvResult = (TextView)findViewById(R.id.tvResult);
        this.tvRPN = (TextView)findViewById(R.id.tvRPN);
    }

    private void setListeners() {
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetUI(v);
                showMessageClearUI(v);
                return true;
            }
        });
    }

    private void resetUI(View view) {
        setInputView("");
        setResultView("");
        setRPNView("");
    }

    private void showMessageClearUI(View v) {
        int location[] = new int[2];
        v.getLocationOnScreen(location);

        Toast ts = Toast.makeText(App.getAppContext(), R.string.msg_clear_ui, Toast.LENGTH_SHORT);
        ts.setGravity(Gravity.TOP|Gravity.LEFT, v.getRight(), location[1]-v.getHeight());
        ts.show();
    }

    private void showMessage(int msgCode) {
        Toast.makeText(App.getAppContext(), msgCode, Toast.LENGTH_LONG).show();
    }

    private void showMessage(String msg) {
        Toast.makeText(App.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }

    private boolean isInputEmpty() {
        return this.tvInput.getText().equals("0");
    }

    private void setInputView(String value) {
        this.tvInput.setText( value.isEmpty() ? "0" : value);
    }

    private void setResultView(String value) {
        this.tvResult.setText( value.isEmpty() ? "" : "= " + value);
    }

    private void setRPNView(String value) {
        this.tvRPN.setText( value.isEmpty() ? "" : "RPN: " + value);
    }
}
package estacio.exercises.av1calc.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

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
                Toast.makeText(MainActivity.this, R.string.exception_expr_not_found, Toast.LENGTH_LONG).show();
            }
        } catch(Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void btnCleanOnClick(View view) {
        setInputView("");
        setResultView("");
        setRPNView("");
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
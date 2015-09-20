package estacio.exercises.av1calc.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import estacio.exercises.av1calc.R;
import estacio.exercises.av1calc.core.CalculatorExpression;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnEqualsOnClick(View view) {
        TextView txtRPN = (TextView)findViewById(R.id.txtRPN);
        EditText edtInput = (EditText)findViewById(R.id.edtInput);
        String input = edtInput.getText().toString();

        try {
            if(!input.isEmpty())
            {
                CalculatorExpression ce = new CalculatorExpression(input);
                edtInput.setText(String.format("%1$,.2f", ce.execute()));
                txtRPN.setText(ce.getRPN());
            } else {
                Toast.makeText(MainActivity.this, R.string.exception_expr_not_found, Toast.LENGTH_LONG).show();
            }
        } catch(Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void btnCleanOnClick(View view) {
        EditText edtInput = (EditText)findViewById(R.id.edtInput);
        TextView txtRPN = (TextView)findViewById(R.id.txtRPN);
        edtInput.setText("");
        txtRPN.setText("");
    }
}

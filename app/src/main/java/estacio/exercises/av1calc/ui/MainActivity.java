package estacio.exercises.av1calc.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import estacio.exercises.av1calc.R;
import estacio.exercises.av1calc.core.CalculatorExpression;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnEqualsOnClick(View view) {
        EditText edtInput = (EditText)findViewById(R.id.edtInput);
        String input = edtInput.getText().toString();

        if(!input.equals("0"))
        {
            CalculatorExpression ce = new CalculatorExpression(input);
            edtInput.setText(ce.execute());
        }
    }
}

package com.appsolutelypositive.openwest;

import android.databinding.DataBindingUtil;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.appsolutelypositive.openwest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static DecimalFormat OPERAND_FORMAT = new DecimalFormat("#.###");

    //Here we import all of our variables that we'll be using, just like any other piece of software .
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This imports the DataBindingUtil object, which presents the Java data in the View
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setOperand("");
        binding.setCurrentLine("");
        binding.setPrevious("");
        binding.setResult("");

        //Button Bindings
        //These binding make it so when the button is clicked, the app "hears" the click
        //request and completes the action by giving a String response, in this case, it's
        //the number on the button that we set up.

        binding.sevenBtn.setOnClickListener(this::updateOperand);
        binding.eightBtn.setOnClickListener(this::updateOperand);
        binding.nineBtn.setOnClickListener(this::updateOperand);
        binding.divBtn.setOnClickListener(this::updateOperand);
        binding.fourBtn.setOnClickListener(this::updateOperand);
        binding.fiveBtn.setOnClickListener(this::updateOperand);
        binding.sixBtn.setOnClickListener(this::updateOperand);
        binding.multBtn.setOnClickListener(this::updateOperand);
        binding.oneBtn.setOnClickListener(this::updateOperand);
        binding.twoBtn.setOnClickListener(this::updateOperand);
        binding.threeBtn.setOnClickListener(this::updateOperand);
        binding.minusBtn.setOnClickListener(this::updateOperand);
        binding.zeroBtn.setOnClickListener(this::updateOperand);
        binding.dotBtn.setOnClickListener(this::updateOperand);

        binding.minusBtn.setOnClickListener(this::updateOperation);
        binding.plusBtn.setOnClickListener(this::updateOperation);
        binding.multBtn.setOnClickListener(this::updateOperation);
        binding.divBtn.setOnClickListener(this::updateOperation);

        binding.equalsBtn.setOnClickListener(view -> computeCalc());

        binding.clearBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(binding.getOperand())) {
                binding.setOperand(trimLast(binding.getOperand()));
            } else if (binding.getPendingOperation() != null) {
                binding.setPendingOperation(null);
                binding.setOperand(binding.getPrevious());
                binding.setPrevious("");
            }
            binding.setCurrentLine(trimLast(binding.getCurrentLine()));
        });

        binding.clearBtn.setOnLongClickListener(view -> {
            binding.setOperand("");
            binding.setCurrentLine("");
            binding.setResult("");
            binding.setPendingOperation(null);
            binding.lastOperation.setText("");
            binding.setPrevious("");
            return true;
        });
    }

    private String trimLast(CharSequence source) {
        return source.subSequence(0, source.length() - 1).toString();
    }

    private void updateOperand(View v) {
        CharSequence value = ((Button) v).getText();
        binding.setOperand(binding.getOperand() + value);
        binding.setCurrentLine(binding.getCurrentLine() + value.toString());
    }

    private void updateOperation(View v) {
        Operation op = Operation.fromSymbol(((Button) v).getText().toString());

        boolean missingOperand = TextUtils.isEmpty(binding.getOperand());
        boolean missingPrevious = TextUtils.isEmpty(binding.getPrevious());

        // No values current typed in
        if (!missingOperand && missingPrevious) {

            binding.setPrevious(binding.getOperand());
            binding.setPendingOperation(op);
            binding.setOperand("");
            binding.setCurrentLine(binding.getCurrentLine() + op.getSymbol());

        } else {

            if (!missingPrevious && !missingOperand) {
                computeCalc();
            }

            binding.setPrevious(binding.getResult());
            binding.setPendingOperation(op);
            binding.setCurrentLine(String.valueOf(binding.getPrevious()) + op.getSymbol());

        }
    }

    private void setLastOperation(double left, double right, Operation op, double result) {
        binding.lastOperation.setText(String.format("%.3f %s %.3f = %.3f",
                left,
                op.getSymbol(),
                right,
                result));
    }

    private void computeCalc() {
        double left = Double.parseDouble(binding.getPrevious());
        double right = Double.parseDouble(binding.getOperand());
        Operation op = binding.getPendingOperation();

        double result = op.apply(left, right);

        binding.setResult(OPERAND_FORMAT.format(result));
        setLastOperation(left, right, op, result);
        binding.setCurrentLine("");
        binding.setPrevious("");
        binding.setOperand("");
        binding.setPendingOperation(null);
    }

}

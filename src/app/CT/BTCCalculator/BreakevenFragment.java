package app.CT.BTCCalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;

public class BreakevenFragment extends SherlockFragment {
    private EditText editFirst;
    private EditText editSecond;
    private EditText editThird;
    private EditText editFourth;
    private EditText editFifth;
    private EditText editSixth;

    private TextView editResultText;
    private TextView editOptimizeText;

    private Button buttonCalculate;
    private Button buttonOptimize;

    private String rate;

    // Function to take the input and round to two decimals.
    public double roundTwoDecimals(double d) {
        DecimalFormat df = new DecimalFormat("###.##");
        return Double.parseDouble(df.format(d));
    }

    // Otto function to subscribe to Event Bus changes.
    @Subscribe
    public void onPriceUpdated(String mRate) {
        rate = mRate;
        //Log.d("Chris", "This is coming from the Fragment: " + rate);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set options menu.
        setHasOptionsMenu(true);
    }

    // Create the view.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Return the view.
        // Log.d("Chris", "onCreateView from BreakevenFragment was called");
        return inflater.inflate(R.layout.first, container, false);
    }

    // Creates the activity for the fragment.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Register Bus Provider instance.
        BusProvider.getInstance().register(this);

        View view = getView();

        // Initialize text fields.
        editFirst = (EditText) view.findViewById(R.id.editFirst);
        editSecond = (EditText) view.findViewById(R.id.editSecond);
        editThird = (EditText) view.findViewById(R.id.editThird);
        editFourth = (EditText) view.findViewById(R.id.editFourth);
        editFifth = (EditText) view.findViewById(R.id.editFifth);
        editSixth = (EditText) view.findViewById(R.id.editSixth);
        editResultText = (TextView) view.findViewById(R.id.resultText);
        editOptimizeText = (TextView) view.findViewById(R.id.optimizeMessage);

        // Initialize buttons.
        buttonCalculate = (Button) view.findViewById(R.id.calculate);
        buttonOptimize = (Button) view.findViewById(R.id.optimize);

        // Checks whether the first visible EditText element is focused in order to enable
        // and show the keyboard to the user. The corresponding XML element has android:imeOptions="actionNext".
        // All EditText elements below are now programmed to show keyboard when pressed.
        editFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFirst.clearFocus();
                editFirst.requestFocus();
                if (editFirst.isFocused()) {
                    // Log.d("Chris", "editFirst onClick, is focused, should have cleared focus, keyboard should show.");
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        editSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSecond.clearFocus();
                editSecond.requestFocus();
                if (editSecond.isFocused()) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        editThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editThird.clearFocus();
                editThird.requestFocus();
                if (editThird.isFocused()) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        editFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFourth.clearFocus();
                editFourth.requestFocus();
                if (editFourth.isFocused()) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        editFifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFifth.clearFocus();
                editFifth.requestFocus();
                if (editFifth.isFocused()) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        editSixth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSixth.clearFocus();
                editSixth.requestFocus();
                if (editSixth.isFocused()) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        // Listens to when "Calculate" button is pressed.
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float buyAmount, buyCost, sellAmount, sellPrice, buyAmount2,
                        buyCost2, remainder, totalCost, totalAmount, finalPrice;
                boolean didItWork = true;
                boolean validTransaction = true;

                /* Dismisses the keyboard.
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                        */

                // Error checking to prevent crashes.
                try {
                    // Gets the input entered from the user.
                    buyAmount = Float.valueOf(editFirst.getText().toString());
                    buyCost = Float.valueOf(editSecond.getText().toString());
                    sellAmount = Float.valueOf(editThird.getText().toString());
                    sellPrice = Float.valueOf(editFourth.getText().toString());
                    buyAmount2 = Float.valueOf(editFifth.getText().toString());
                    buyCost2 = Float.valueOf(editSixth.getText().toString());

                    // Calculates remainder from the buying and selling.
                    remainder = Math.abs((buyAmount - sellAmount));

                    // Checks if the user typed in a greater selling amount than buying.
                    if (sellAmount > buyAmount) {
                        // Create new dialog popup.
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                        // Sets title.
                        alertDialog.setTitle("Error");

                        // Sets dialog message.
                        alertDialog.setMessage("You cannot sell more than you own.");
                        alertDialog.setCancelable(false);
                        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // If this button is clicked, close current dialog.
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();

                        // Invalidates transaction since selling amount > buying amount.
                        validTransaction = false;
                    }

                    // Calculations to output.
                    totalCost = buyAmount * buyCost;
                    totalAmount = buyAmount2 + remainder;
                    finalPrice = totalCost / totalAmount;

                    // Checks if valid.
                    if (validTransaction) {
                        editResultText.setText("You need to sell " + String.valueOf(totalAmount) + " BTC at $" +
                                String.valueOf(roundTwoDecimals(finalPrice)) + " to break-even.");
                    }
                } catch (Exception e) {
                    // Sets bool to false in order to execute "finally" block below.
                    didItWork = false;
                } finally {
                    if (!didItWork) {
                        // Creates new dialog popup.
                        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getActivity());

                        // Sets title.
                        alertDialog2.setTitle("Error");

                        // Sets dialog message.
                        alertDialog2.setMessage("Please fill in all fields.");
                        alertDialog2.setCancelable(false);
                        alertDialog2.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // If this button is clicked, close current dialog.
                                dialog.cancel();
                            }
                        });

                        // Show the dialog.
                        alertDialog2.show();
                    }
                }

            }
        });

        buttonOptimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float sellAmount, sellPrice, buyCost2, newBalance, optimalBTC;
                boolean didItWork = true;

                // Dismisses the keyboard.
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                // Error checking to prevent crashes.
                try {
                    // Gets the input entered from the user.
                    sellAmount = Float.valueOf(editThird.getText().toString());
                    sellPrice = Float.valueOf(editFourth.getText().toString());
                    buyCost2 = Float.valueOf(editSixth.getText().toString());

                    newBalance = sellAmount * sellPrice;
                    optimalBTC = newBalance / buyCost2;

                    editOptimizeText.setText("The optimal BTC you should buy is " + String.valueOf(roundTwoDecimals(optimalBTC)) + ".");
                } catch (Exception e) {
                    // Sets bool to false in order to execute "finally" block below.
                    didItWork = false;
                } finally {
                    if (!didItWork) {
                        // Create new dialog popup.
                        final AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(getActivity());

                        // Sets title.
                        alertDialog3.setTitle("Error");

                        // Sets dialog message.
                        alertDialog3.setMessage("Please look at the note.");
                        alertDialog3.setCancelable(false);
                        alertDialog3.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // If this button is clicked, close current dialog.
                                dialog.cancel();
                            }
                        });

                        // Show dialog
                        alertDialog3.show();
                    }
                }
            }
        });
    }

    // Create Options Menu.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    // When Options Menu items are selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AddCurrent:
                AddCurrent();
                break;
            case R.id.RemoveCurrent:
                RemoveCurrent();
                break;
        }
        return false;
    }

    // Adds the current price to the text field.
    public void AddCurrent() {
        EditText editSecond;
        View v = getView();

        editSecond = (EditText) v.findViewById(R.id.editSecond);

        editSecond.setText(rate);

        //Log.d("Chris", "Called in fragment.");
    }

    // Removes the price and sets text field to default.
    public void RemoveCurrent() {
        EditText editSecond;
        View v = getView();

        editSecond = (EditText) v.findViewById(R.id.editSecond);

        editSecond.setText("");

        //Log.d("Chris", "Called in fragment.");
    }
}

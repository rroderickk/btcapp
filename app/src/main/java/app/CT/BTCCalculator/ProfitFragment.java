package app.CT.BTCCalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;

public class ProfitFragment extends Fragment {
    // Declare variables for this class.
    private EditText btcBought;
    private EditText btcBoughtPrice;
    private EditText btcSell;
    private EditText btcSellPrice;
    private EditText transPercent;

    private SeekBar transFeeSeekBar;

    private TextView feeTransResult;
    private TextView subtotalResult;
    private TextView totalProfitResult;

    private String rate;

    private boolean[] containsCurrentRate = {false, false};

    // Otto function to subscribe to Event Bus changes.
    @Subscribe
    public void onPriceUpdated(String mRate) {
        rate = mRate;

        // If btcBoughtPrice field has the current price, update it.
        if (containsCurrentRate[0]) btcBoughtPrice.setText(rate);
        // If btcSellPrice has the current price, update it as well.
        if (containsCurrentRate[1]) btcSellPrice.setText(rate);
        //Log.d("Chris", "This is coming from the ProfitFragment: " + rate);
    }

    // Function round to two decimals.
    public double roundTwoDecimals(double d) {
        DecimalFormat df = new DecimalFormat("###.##");
        return Double.parseDouble(df.format(d));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set options menu.
        setHasOptionsMenu(true);
    }

    /* Called when the activity is attached to this fragment.
    @Override
    public void onAttach(Activity activity) {
        // Call to the super class.
        super.onAttach(activity);
    }*/

    // Create the view.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout from the XML Resource.
        return inflater.inflate(R.layout.fragment_profit, container, false);
    }

    // Creates the activity for the fragment.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Call to the super class.
        super.onActivityCreated(savedInstanceState);

        // Get View.
        View view = getView();

        // Register Bus Provider instance.
        BusProvider.getInstance().register(this);

        // Initialize text fields.
        assert view != null;
        btcBought = (EditText) view.findViewById(R.id.btcBought);
        btcBoughtPrice = (EditText) view.findViewById(R.id.btcBoughtPrice);
        btcSell = (EditText) view.findViewById(R.id.btcSell);
        btcSellPrice = (EditText) view.findViewById(R.id.btcSellPrice);
        transPercent = (EditText) view.findViewById(R.id.transPercent);

        transFeeSeekBar = (SeekBar) view.findViewById(R.id.transFeeSeekBar);

        feeTransResult = (TextView) view.findViewById(R.id.transFeeCost);
        subtotalResult = (TextView) view.findViewById(R.id.subtotal);
        totalProfitResult = (TextView) view.findViewById(R.id.totalProfit);

        Button calculate = (Button) view.findViewById(R.id.calculate);

        // Initialize percentage variable which is attached to transFeeSeekBar and transPercent.
        final float[] percentage = new float[1];
        percentage[0] = (float) 0.0;

        // EditText element is clicked in order to enable and show the keyboard to the user.
        // The corresponding XML element has android:imeOptions="actionNext".
        // All EditText elements below are now programmed to show keyboard when pressed.
        btcBought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftKeyboard(v);
            }
        });

        btcBoughtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftKeyboard(v);
            }
        });

        btcSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftKeyboard(v);
            }
        });

        btcSellPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftKeyboard(v);
            }
        });

        btcBoughtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                containsCurrentRate[0] = false;
            }
        });

        btcSellPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                containsCurrentRate[1] = false;
            }
        });

        // Listens for the transPercent field to change in order to update the seek bar.
        transPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    // Update transFeeSeekBar value after entering a number
                    transFeeSeekBar.setProgress(Integer.parseInt(s.toString()));
                    // Log.d("Chris", "transPercent.addTextChangedListener, percentage = " + percentage[0]);
                } catch (Exception ignored) {
                }
            }
        });

        transFeeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar transFeeSeekBar, final int progress, boolean fromUser) {
                percentage[0] = progress;
                // Log.d("Chris", "transFeeSeekBar.setOnSeekBarChangeListener before, percentage = " + percentage[0]);
                transPercent.setText(String.valueOf(progress));

                // Sets the transPercent selection at the end of the input.
                transPercent.post(new Runnable() {
                    @Override
                    public void run() {
                        transPercent.setSelection(String.valueOf(progress).length());
                    }
                });
                // Log.d("Chris", "transFeeSeekBar.setOntransFeeSeekBarChangeListener after, percentage = " + percentage[0]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar transFeeSeekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar transFeeSeekBar) {
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float buyAmount, buyCost, sellAmount, sellPrice,
                        subtotalCost, subtotalPrice, subtotal, fee;
                float total = 0;

                boolean validTrans = true;

                boolean didItWork = true;

                /* Dismisses the keyboard.
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                        */


                // Error checking to prevent crashes.
                try {
                    // Gets the input entered from the user.
                    buyAmount = Float.valueOf(btcBought.getText().toString());
                    buyCost = Float.valueOf(btcBoughtPrice.getText().toString());
                    sellAmount = Float.valueOf(btcSell.getText().toString());
                    sellPrice = Float.valueOf(btcSellPrice.getText().toString());

                    percentage[0] = (float) ((Float.valueOf(transPercent.getText().toString())) / 100.0);
                    percentage[0] = (float) (transFeeSeekBar.getProgress() / 100.0);
                    // Log.d("Chris", "calculate.setOnClickListener, percentage = " + percentage[0]);

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

                        validTrans = false;
                    }

                    // Calculations to output.
                    subtotalCost = buyAmount * buyCost;
                    subtotalPrice = sellPrice * sellAmount;
                    subtotal = subtotalPrice - subtotalCost;

                    fee = subtotal * percentage[0];
                    total = subtotal - fee;

                    if (validTrans) {
                        feeTransResult.setText(String.format("$%s", String.valueOf(roundTwoDecimals(fee))));
                        subtotalResult.setText(String.format("$%s", String.valueOf(roundTwoDecimals(subtotal))));
                        totalProfitResult.setText(String.format("$%s", String.valueOf(roundTwoDecimals(total))));
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

    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    // Create Options Menu.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    // When Options Menu items are selected; adds or removes the current price from their corresponding fields.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
            0 represents the buy field
            1 represents the sell field
        */
        switch (item.getItemId()) {
            case R.id.editCurrentBuyField: {

                try {
                    EditText btcBoughtPrice = (EditText) getView().findViewById(R.id.btcBoughtPrice);

                    // If field contains the current price, remove it; else, add the current price.
                    if (containsCurrentRate[0]) {
                        btcBoughtPrice.setText("");
                        containsCurrentRate[0] = false;
                    } else {
                        btcBoughtPrice.setText(rate);
                        containsCurrentRate[0] = true;
                    }
                } catch (Exception ignored) {
                }

                return true;
            }
            case R.id.editCurrentSellField: {

                try {
                    EditText btcSellPrice = (EditText) getView().findViewById(R.id.btcSellPrice);

                    // If field contains the current price, remove it; else, add the current price.
                    if (containsCurrentRate[1]) {
                        btcSellPrice.setText("");
                        containsCurrentRate[1] = false;
                    } else {
                        btcSellPrice.setText(rate);
                        containsCurrentRate[1] = true;
                    }
                }
                catch (Exception ignored) {
                }

                return true;
            }
            case R.id.resetAll: {
                btcBought.setText("");
                btcBoughtPrice.setText("");
                btcSell.setText("");
                btcSellPrice.setText("");
                transPercent.setText("");
                feeTransResult.setText("$");
                subtotalResult.setText("$");
                totalProfitResult.setText("$");
                containsCurrentRate[0] = containsCurrentRate[1] = false;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

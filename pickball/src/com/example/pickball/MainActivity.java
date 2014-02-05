package com.example.pickball;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Random;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;

import android.graphics.Paint;
import android.graphics.Typeface;

import android.util.AttributeSet;

import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	// variable creation part
	EditText etTemp;
	TextView tvTemp, tvTitle, tvLSResult, tvLocal, tvCorrect;
	ImageButton ibStart, ibT;
	LinearLayout llTemp, llText, llCorrect, llResult, llGifViewT,
			llGlassLayout;
	LinearLayout.LayoutParams layoutParams, lpTextViewLS, lpGifViewT,
			llResultParams, llGlassParams, layoutParamsCir;
	Context _context;
	boolean bStartAgain, bLengthFix, bGameWin, bWinner, bGameText,
			bIsGameStart, bCheckText;
	int iLengthFirst, iLengthBar, iTotalLuckRatio, iTotalPlay, iAvgLuckRatio,
			iClickCount;
	int iMatch, iNonMatch, iTextLength, iILBStart, iLBGameWin, iL, iLT, iL1,
			iA[], iPercent, iNew, iSLength;
	ImageView ivGreenGlass, ivRedGlass;
	MediaPlayer mpTemp;
	ArrayList<UserGameHistory> alGameHistory;
	UserGameHistory ughTemp;
	String sUserGameText, sText, sTextNew, sMessage;
	CustomView cvT;
	Random rNo;

	// native code function and library loading
	public native int[] randomUnique(int isl);

	public native boolean charFrequencyTest(String scheck);

	public native boolean checkSpecialCharacter(String scheck);

	static {
		System.loadLibrary("pickball");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// activity start loading work.
		startActivityLoadingWork();
		ibStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// game start on click block
				gameStartOnClickBlock();
			}
		});
	}

	// activity start loading work.
	private void startActivityLoadingWork() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.customtitlebar);
		tvTitle = (TextView) findViewById(R.id.title_text);
		tvTitle.setText(R.string.title_activity_main);
		_context = this.getApplicationContext();
		iMatch = iNonMatch = iTextLength = iILBStart = iLBGameWin = iL = iL1 = iPercent = 0;
		iLengthBar = iTotalLuckRatio = iTotalPlay = iAvgLuckRatio = iLT = iSLength = iClickCount = 0;
		bStartAgain = bGameWin = bIsGameStart = bCheckText = false;
		iA = null;
		cvT = null;
		etTemp = (EditText) findViewById(R.id.txtAnyString);
		tvTemp = (TextView) findViewById(R.id.tvAnyString);
		ibStart = (ImageButton) findViewById(R.id.btnAnyString);
		tvTemp.setText("");
		llTemp = (LinearLayout) findViewById(R.id.buttonlayout);
		llText = (LinearLayout) findViewById(R.id.textlayout);
		llCorrect = (LinearLayout) findViewById(R.id.textCorrectLayout);
		llResult = (LinearLayout) findViewById(R.id.resultLayout);
		llGifViewT = (LinearLayout) findViewById(R.id.llGifView);
		llGlassLayout = (LinearLayout) findViewById(R.id.glassLayout);
		layoutParams = new LinearLayout.LayoutParams(40, 40);
		layoutParams.setMargins(1, 1, 1, 1);
		layoutParamsCir = new LinearLayout.LayoutParams(40, 40);
		llResultParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 30);
		lpGifViewT = new LinearLayout.LayoutParams(311, 68);
		lpTextViewLS = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				25);
		lpTextViewLS.setMargins(0, 5, 0, 5);
		llGlassParams = new LinearLayout.LayoutParams(81, 99);
		llGlassParams.setMargins(10, 3, 10, 3);

		ivGreenGlass = new ImageView(_context);
		ivGreenGlass.setImageResource(R.drawable.g0);
		llGlassLayout.addView(ivGreenGlass, llGlassParams);
		ivRedGlass = new ImageView(_context);
		ivRedGlass.setImageResource(R.drawable.r0);
		llGlassLayout.addView(ivRedGlass, llGlassParams);

		ImageView ivTLM = new ImageView(_context);
		ivTLM.setImageResource(R.drawable.lmtwo);
		llGifViewT.addView(ivTLM, lpGifViewT);
		etTemp.setBackgroundResource(R.drawable.doltextbg);

		llResult.removeAllViews();
		tvLSResult = new TextView(_context);
		tvLSResult.setBackgroundResource(R.drawable.lscale);
		tvLSResult.setText(R.string.tv_text_ls);
		tvLSResult.setTextColor(Color.BLACK);
		tvLSResult.setTypeface(null, Typeface.BOLD_ITALIC);
		tvLSResult.setTextSize(13f);
		llResult.addView(tvLSResult, lpTextViewLS);
		alGameHistory = new ArrayList<UserGameHistory>();
		bGameText = false;
	}

	// game start on click block
	private void gameStartOnClickBlock() {
		// set background and text color
		tvTemp.setBackgroundDrawable(null);
		tvTemp.setTextColor(Color.WHITE);
		// if user want to break or quit game, not allowed in middle of
		// game
		if (bIsGameStart) {
			Toast.makeText(_context,
					"Not break, please click all feather buttons.",
					Toast.LENGTH_LONG).show();
			return;
		}
		iTextLength = etTemp.getText().toString().trim().length();
		// game text checking action first time, special character,
		// frequency and length
		if (bCheckText == false) {
			if (resultFrequencyTest(etTemp.getText().toString().trim())) {
				Toast.makeText(_context, sMessage, Toast.LENGTH_LONG).show();
				return;
			}
			bCheckText = true;
		}
		// continue if length>0
		if (iTextLength > 0) {
			if (bGameText == false) {
				// first time loading
				fristTimeLoad();
			}
			iTotalPlay += 1;
			iClickCount = 0;
			llTemp.removeAllViews();
			llText.removeAllViews();
			llCorrect.removeAllViews();
			// game loop start again action on click
			startGameAgain();

			tvTemp.setText(etTemp.getText());
			sText = tvTemp.getText().toString().trim();
			iL = sText.length();
			iLengthFirst = iL;

			// ndk function call
			iA = randomUnique(iL);

			sTextNew = tvTemp.getText().toString();
			// text view creation for correct and random
			textViewCreation();
			// set TextView empty text string
			tvTemp.setText("");
			// image button creation and click creation
			imageButtonCreation();
		} else {
			iLengthBar = iTotalLuckRatio = iTotalPlay = iAvgLuckRatio = iMatch = iNonMatch = 0;
			bStartAgain = bGameWin = false;
			bWinner = false;
			tvTemp.setText("Enter any 9 char string.");
			llTemp.removeAllViews();
			llText.removeAllViews();
			llCorrect.removeAllViews();
			llResult.removeAllViews();
			ivGreenGlass.setImageResource(R.drawable.g0);
			ivRedGlass.setImageResource(R.drawable.r0);
		}
	}

	// first time loading code
	private void fristTimeLoad() {
		iMatch = iNonMatch = iILBStart = iLBGameWin = iL = iL1 = iPercent = 0;
		iLengthBar = iTotalLuckRatio = iTotalPlay = iAvgLuckRatio = iLT = 0;
		bGameText = true;
		sUserGameText = etTemp.getText().toString();
		etTemp.setVisibility(View.INVISIBLE);
		tvTemp.setVisibility(View.INVISIBLE);
		llTemp.removeAllViews();
		llText.removeAllViews();
		llCorrect.removeAllViews();
		llResult.removeAllViews();
		tvLSResult = new TextView(_context);
		tvLSResult.setBackgroundResource(R.drawable.lscale);
		tvLSResult.setText(R.string.tv_text_ls);
		tvLSResult.setTextColor(Color.BLACK);
		tvLSResult.setTypeface(null, Typeface.BOLD_ITALIC);
		tvLSResult.setTextSize(13f);
		llResult.addView(tvLSResult, lpTextViewLS);
		ivGreenGlass.setImageResource(R.drawable.g0);
		ivRedGlass.setImageResource(R.drawable.r0);
	}

	// game loop start again action on click
	private void startGameAgain() {
		if (bStartAgain == true) {
			bStartAgain = false;
			bGameWin = false;
			iMatch = iNonMatch = 0;
			ivGreenGlass.setImageResource(R.drawable.g0);
			ivRedGlass.setImageResource(R.drawable.r0);
			iILBStart = tvTemp.getText().toString().trim().length();
			iILBStart = iLengthFirst - iILBStart;
			if (iILBStart == 0) {
				iILBStart = 0;
			}
			iPercent = (iILBStart * 100) / iLengthFirst;
			if (iPercent > 0) {
				iLengthBar += iPercent;
				iTotalLuckRatio += iPercent;
				llResult.removeAllViews();
				tvLSResult = new TextView(_context);
				tvLSResult.setBackgroundResource(R.drawable.lscale);
				tvLSResult.setText(R.string.tv_text_ls);
				tvLSResult.setTextColor(Color.BLACK);
				tvLSResult.setTypeface(null, Typeface.BOLD_ITALIC);
				tvLSResult.setTextSize(13f);
				llResult.addView(tvLSResult, lpTextViewLS);
				cvT = new CustomView(_context, iLengthBar);
				llResult.addView(cvT, llResultParams);
			}
			ughTemp = new UserGameHistory();
			ughTemp.setGameText(tvTemp.getText().toString());
			ughTemp.setGameLuck(iPercent);
			alGameHistory.add(ughTemp);
		}
	}

	// text view creation for correct and random
	private void textViewCreation() {
		for (int iS = 0; iS < iL; iS++) {

			tvLocal = new TextView(_context);
			tvLocal.setText(String.valueOf(sTextNew.charAt(iA[iS])));
			tvLocal.setGravity(Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL);
			tvLocal.setBackgroundResource(R.drawable.cir_red);
			tvLocal.setTypeface(null, Typeface.BOLD);
			tvLocal.setTextColor(Color.WHITE);
			tvLocal.setVisibility(View.INVISIBLE);
			llText.addView(tvLocal, layoutParamsCir);

			tvCorrect = new TextView(_context);
			tvCorrect.setText(String.valueOf(sTextNew.charAt(iS)));
			tvCorrect.setGravity(Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL);
			tvCorrect.setBackgroundResource(R.drawable.cir_green);
			tvCorrect.setTextColor(Color.WHITE);
			tvCorrect.setTypeface(null, Typeface.BOLD);
			llCorrect.addView(tvCorrect, layoutParamsCir);
		}
		iA=null;
	}

	// feather image button creation
	private void imageButtonCreation() {
		for (int iS = 0; iS < iL; iS++) {
			final int iSIndex = iS;
			ImageButton btnImage = new ImageButton(_context);
			btnImage.setImageResource(R.drawable.btn30x30black);
			btnImage.setBackgroundDrawable(null);
			llTemp.addView(btnImage, layoutParams);
			final TextView tvN = (TextView) llText.getChildAt(iS);
			final String sV = tvN.getText().toString();
			btnImage.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					bIsGameStart = true;
					ibStart.setVisibility(View.INVISIBLE);
					ibT = (ImageButton) llTemp.getChildAt(iSIndex);
					ibT.setVisibility(View.INVISIBLE);
					TextView tvC = (TextView) llCorrect.getChildAt(iClickCount);
					String sC = tvC.getText().toString();
					iClickCount = iClickCount + 1;
					tvN.setVisibility(View.VISIBLE);
					if (sV.equalsIgnoreCase(sC)) {
						tvC.setBackgroundResource(R.drawable.cir_green);
						tvN.setBackgroundResource(R.drawable.cir_green);
						iMatch = iMatch + 1;
						// check match character
						checkMatch();
						bGameWin = true;
						// user game win calling
						userGameWin();
					} else {
						bStartAgain = true;
						iNonMatch = iNonMatch + 1;
						tvC.setBackgroundResource(R.drawable.cir_red);
						tvN.setBackgroundResource(R.drawable.cir_red);
						String sF = tvTemp.getText().toString();
						sF = sF.concat(sC);
						tvTemp.setText(sF);
						etTemp.setText(sF);
						// check non match character
						checkNonMatch();
					}
					if (iTextLength == iClickCount) {
						bIsGameStart = false;
					}
					if (iNonMatch > 0) {
						if (iTextLength == (iMatch + iNonMatch)) {
							ibStart.setImageResource(R.drawable.btn_palyagain);
							// ibStart.getLayoutParams().width = 125;
							ibStart.setVisibility(View.VISIBLE);
						}
					}
				}
			});
		}
	}

	// game win coding function
	private void userGameWin() {
		if (iTextLength == iMatch && bGameWin == true) {
			ibStart.setImageResource(R.drawable.btnstart);
			// ibStart.getLayoutParams().width = 80;
			ibStart.setVisibility(View.VISIBLE);
			etTemp.setVisibility(View.VISIBLE);
			tvTemp.setVisibility(View.VISIBLE);
			tvTemp.setText("");
			etTemp.setText("");
			iLBGameWin = tvTemp.getText().toString().trim().length();
			if (iLBGameWin == 0) {
				iTotalLuckRatio += 100;
				iLengthBar += 100;
				tvTemp.setTextColor(Color.BLACK);
				tvTemp.setBackgroundResource(R.drawable.winbg);
				tvTemp.setText("WON");
				iAvgLuckRatio = iTotalLuckRatio / iTotalPlay;
				llResult.removeAllViews();
				tvLSResult = new TextView(_context);
				tvLSResult.setBackgroundResource(R.drawable.lscale);
				tvLSResult.setText(R.string.tv_text_ls);
				String sLS = tvLSResult.getText().toString();
				sLS = sLS.concat("(" + String.valueOf(iAvgLuckRatio) + "%)");
				tvLSResult.setText(sLS);
				tvLSResult.setTextColor(Color.BLACK);
				tvLSResult.setTypeface(null, Typeface.BOLD_ITALIC);
				tvLSResult.setTextSize(13f);
				llResult.addView(tvLSResult, lpTextViewLS);
				cvT = new CustomView(_context, iLengthBar, iAvgLuckRatio);
				llResult.addView(cvT, llResultParams);
			}
			iLengthBar = iTotalLuckRatio = iTotalPlay = iMatch = iNonMatch = 0;
			bStartAgain = bGameWin = bGameText = bIsGameStart = false;
			bCheckText = true;
			ughTemp = new UserGameHistory();
			ughTemp.setGameText(tvTemp.getText().toString());
			ughTemp.setGameLuck(100);
			alGameHistory.add(ughTemp);
			storeLastGameHistory(alGameHistory, iAvgLuckRatio);
			iAvgLuckRatio = 0;
			sUserGameText = "";
			mpTemp = MediaPlayer.create(_context, R.raw.ding);
			mpTemp.start();
		}
	}

	// check match character counting
	private void checkMatch() {
		if (iMatch == 1) {
			ivGreenGlass.setImageResource(R.drawable.g1);
			return;
		}
		if (iMatch == 2) {
			ivGreenGlass.setImageResource(R.drawable.g2);
			return;
		}
		if (iMatch == 3) {
			ivGreenGlass.setImageResource(R.drawable.g3);
			return;
		}
		if (iMatch == 4) {
			ivGreenGlass.setImageResource(R.drawable.g4);
			return;
		}
		if (iMatch == 5) {
			ivGreenGlass.setImageResource(R.drawable.g5);
			return;
		}
		if (iMatch == 6) {
			ivGreenGlass.setImageResource(R.drawable.g6);
			return;
		}
		if (iMatch == 7) {
			ivGreenGlass.setImageResource(R.drawable.g7);
			return;
		}
		if (iMatch == 8) {
			ivGreenGlass.setImageResource(R.drawable.g8);
			return;
		}
		if (iMatch == 9) {
			ivGreenGlass.setImageResource(R.drawable.g9);
			return;
		}
	}

	// check non-match character counting
	private void checkNonMatch() {
		if (iNonMatch == 1) {
			ivRedGlass.setImageResource(R.drawable.r1);
			return;
		}
		if (iNonMatch == 2) {
			ivRedGlass.setImageResource(R.drawable.r2);
			return;
		}
		if (iNonMatch == 3) {
			ivRedGlass.setImageResource(R.drawable.r3);
			return;
		}
		if (iNonMatch == 4) {
			ivRedGlass.setImageResource(R.drawable.r4);
			return;
		}
		if (iNonMatch == 5) {
			ivRedGlass.setImageResource(R.drawable.r5);
			return;
		}
		if (iNonMatch == 6) {
			ivRedGlass.setImageResource(R.drawable.r6);
			return;
		}
		if (iNonMatch == 7) {
			ivRedGlass.setImageResource(R.drawable.r7);
			return;
		}
		if (iNonMatch == 8) {
			ivRedGlass.setImageResource(R.drawable.r8);
			return;
		}
		if (iNonMatch == 9) {
			ivRedGlass.setImageResource(R.drawable.r9);
			return;
		}
	}

	// check all character frequency
	// check input character range (a-z,A-z and 0-9) only.
	// check string length also. fix length=9

	private boolean resultFrequencyTest(String scheck) {
		boolean bresult = false;
		int isl = scheck.length();
		if (isl >= 5 && isl <= 7) {

			// ndk function call
			bresult = checkSpecialCharacter(scheck);

			if (bresult == true) {
				sMessage = "Special character is not allowed.";
			}
			if (bresult == false) {
				for (int istart = 0; istart < isl; istart++) {

					// ndk function call
					bresult = charFrequencyTest(scheck);

					if (bresult == true) {
						sMessage = "Any character max frequency is 2 only.";
						break;
					}
				}
			}
		} else {
			sMessage = "Input string length is min 5 and max 7.";
			bresult = true;
		}
		return bresult;
	}

	// / save last game history.
	private void storeLastGameHistory(ArrayList<UserGameHistory> altemp,
			int iagv) {
		final String FILENAME = "GameHistory.txt";
		String swritehistory = "";
		int icount = 0;
		String sbody = "body {margin:2px;border: 2px dotted #61210B;} ";
		String sstyle = sbody
				+ "table{margin:3px;border: 2px solid #0B0540;}th{background-color:#0E3AB2;color:#F5F6F8;}td{background-color:#DBD9F0;} h4{margin:5px;background-color:#59AA5B;color:#ffffff;} h3{margin:5px;background-color:#424242;color:#ffffff;}";
		try {
			FileOutputStream fos = _context.openFileOutput(FILENAME,
					Context.MODE_PRIVATE);

			for (UserGameHistory ughlocal : altemp) {
				icount = icount + 1;
				swritehistory += "<tr><td>&nbsp;&nbsp;&nbsp;"
						+ String.valueOf(icount) + "</td><td>&nbsp;"
						+ ughlocal.getGameText() + "</td><td align='right'>"
						+ String.valueOf(ughlocal.getGameLuck())
						+ "&nbsp;&nbsp;</td></tr>";
			}

			swritehistory = "<html><head><style type='text/css'>"
					+ sstyle
					+ "</style></head><body><center><h3>"
					+ sUserGameText
					+ "</h3><table width='90%' border='1px'><tr><th>Chance</th><th>Text</th><th align='right'>Result(%)&nbsp;&nbsp;</th></tr>"
					+ swritehistory + "</table><h4>Result Avg. "
					+ String.valueOf(iagv) + "%</h4></center></body></html>";
			fos.write(swritehistory.getBytes());
			fos.close();
			altemp.clear();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	// menu loading
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		setMenuBackground();
		return true;
	}

	// select menu click action
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent it = null;
		int resultCode = -1;
		switch (item.getItemId()) {
		case R.id.menu_help:
			it = new Intent(_context, VeiwGameHelp.class);
			startActivity(it);
			return true;
		case R.id.menu_new:
			if (resultCode == Activity.RESULT_OK) {
				finish();
				startActivity(getIntent());
			}
			return true;
		case R.id.menu_stop:
			if (resultCode == Activity.RESULT_OK) {
				finish();
			}
			return true;
		case R.id.menu_history:
			it = new Intent(_context, ViewGameHistory.class);
			startActivity(it);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// / set menu background
	protected void setMenuBackground() {

		getLayoutInflater().setFactory(new Factory() {
			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
					try {
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);
						new Handler().post(new Runnable() {
							public void run() {
								view.setBackgroundColor(Color.BLACK);
							}
						});
						return view;
					} catch (InflateException e) {
					} catch (ClassNotFoundException e) {
					}
				}
				return null;
			}
		});
	}

	// create class CustomView for result scale
	private class CustomView extends View {
		int _ix;
		int _ixavg;

		public CustomView(Context context) {
			super(context);
			_ix = 0;
			_ixavg = 0;
		}

		public CustomView(Context context, int ix) {
			super(context);
			_ix = ix;
			_ixavg = 0;
		}

		public CustomView(Context context, int ix, int ixavg) {
			super(context);
			_ix = ix;
			_ixavg = ixavg;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStrokeWidth(25);
			paint.setColor(Color.WHITE);
			canvas.drawLine(0, 0, _ix, 0, paint);
			paint.setPathEffect(new DashPathEffect(new float[] { 2, 4 }, 25));
			paint.setColor(Color.rgb(0, 0, 0));
			canvas.drawLine(0, 0, _ix, 0, paint);
			if (_ixavg > 0) {
				paint.setColor(Color.RED);
				canvas.drawLine(0, 0, _ixavg, 0, paint);
			}

		}
	}
}

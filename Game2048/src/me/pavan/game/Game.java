package me.pavan.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Game {

	private int NUM_MATRIX = 3;

	private static int[][] gameArray;

	public static final int ACTION_UP = 1, ACTION_DOWN = 2, ACTION_LEFT = 3,
			ACTION_RIGHT = 4;

	public static void main(String[] args) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the size of array (more than 3)");
		try {
			int matrixLength = Integer.parseInt(br.readLine());
			createArray(matrixLength);
			startGame(matrixLength);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void createArray(int matrixLength) {
		gameArray = new int[matrixLength][matrixLength];
		for (int i = 0; i < matrixLength; i++) {
			for (int j = 0; j < matrixLength; j++) {
				gameArray[i][j] = 0;
			}
		}
		int randomI = 0, randomJ = 0;
		int randomI1 = 0, randomJ1 = 0;
		randomI = new Random().nextInt(matrixLength - 1);
		randomJ = new Random().nextInt(matrixLength - 1);
		gameArray[randomI][randomJ] = 2;
		while (randomI != randomI1 && randomJ != randomJ1) {
			randomI = new Random().nextInt(matrixLength - 1);
			randomJ = new Random().nextInt(matrixLength - 1);
		}
		gameArray[randomI1][randomJ1] = 2;
		printArray(matrixLength);
	}

	private static void printArray(int matrixLength) {
		for (int i = 0; i < matrixLength; i++) {
			for (int j = 0; j < matrixLength; j++) {
				System.out.print(gameArray[i][j] + " ");
				if (j == matrixLength - 1) {
					System.out.println("\n");
				}
			}
		}
	}

	public static void startGame(int matrixLength) {
		int action = 0;
		while (true) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.println("Enter action 1- up 2- down 3- left 4 - right");
			try {
				action = Integer.parseInt(br.readLine());
			} catch (Exception e) {
				e.printStackTrace();
			}
			boolean status = processAction(action, matrixLength);
			if (!status) {
				break;
			}
		}
	}
		
	private static boolean processAction(int action, int matrixLength) {

		switch (action) {
		case ACTION_UP:
			// move elements up and
			// check all elements in the columns from top
			// if 2 elements are the same then add them and move again else stop

			for (int j = 0; j < matrixLength; j++) {
				for (int i = 1; i < matrixLength; i++) {
					if (gameArray[i][j] == 0) {
						for (int k = i + 1; k < matrixLength; k++) {
							if (gameArray[k][j] != 0) {
								System.out.println("i j k j" + i + " " + j
										+ " " + k + " " + j);
								swap(i, j, k, j);
							}
						}

					}
				}
			}

			printArray(matrixLength);
			// for (int j = 0; j < matrixLength; j++) {
			// for (int i = 0; i < matrixLength; i++) {
			// if (gameArray[i][j] != 0) {
			// }
			// }
			// }
			break;
		case ACTION_DOWN:
			// move elements down and
			// check all elements in the columns from bottom
			// if 2 elements are the same then add them and move again
			break;
		case ACTION_LEFT:
			// move elements up and
			// check all elements in the rows from left
			// if 2 eelments are the same then add them and move again
			break;
		case ACTION_RIGHT:
			// move elements up and
			// check all elements in the rows from right
			// if 2 eelments are the same then add them and move again
			break;

		}

		boolean isNextMovePossible = checkIf2048Formed();
		return isNextMovePossible;
	}

	private static void swap(int i1, int j1, int i2, int j2) {

		int temp = gameArray[i1][j1];
		gameArray[i1][j1] = gameArray[i2][j2];
		gameArray[i2][j2] = temp;
	}

	private static boolean checkIf2048Formed() {

		// complete array if 2048 is present or if there is any empty places
		return true;
	}
}

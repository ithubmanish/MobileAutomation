package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

//        int arr[] = new int[5];
//        arr[0] = 1;
//        arr[1] = 2;
//        arr[2] = 3;
//        arr[3] = 4;
//        arr[4] = 5;
//
//        for (int i = 0; i < arr.length; i++) {
//            System.out.println("Array value at arr[" + i + "]: " + arr[i]);
//        }

        Scanner sc = new Scanner(System.in);
        int rows = sc.nextInt();
        int col = sc.nextInt();
        int[][] array2D = new int[rows][col];

        for(int i=0;i<rows;i++){
            for(int j=0;j<col;j++){
                array2D[i][j]=sc.nextInt();
            }
        }

        for(int i=0;i<rows;i++){
            for(int j=0;j<col;j++){
                System.out.print(array2D[i][j]);
            }
            System.out.println();
        }

    }

    public static String reverseString(String str) {
        String rev = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            rev = rev + str.charAt(i);
        }
        return rev;
    }
}
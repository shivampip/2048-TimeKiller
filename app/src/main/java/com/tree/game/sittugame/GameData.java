package com.tree.game.sittugame;

import java.util.Random;

class GameData {


    final int n;
    final int[][] a;
    private int score = 0;
    private int best = 0;
    private int level = 0;
    int number = 5;
    private final int[] series = new int[20];
    private int target=0;


    public int getScore() {
        return score;
    }

    public int getBest() {
        return best;
    }

    public int getLevel() {
        return level;
    }

    public void setBest(int best) {
        this.best = best;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel(int seriess) {
        for (int i = 0; i < 20; i++) {
            if (series[i] == seriess) {
                return i;
            }
        }
        return 0;
    }

    public int getTarget(){
        return target;
    }

    //-------------------Initialize zero to all--------------------------------
    public GameData(int n, int number) {
        this.n = n;
        this.number = number;
        this.level = 0;
        a = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = 0;
            }
        }

        int temp = number;
        series[0] = temp;
        for (int i = 1; i < 20; i++) {
            series[i] = temp *= 2;
            System.out.print(" " + i + ", " + series[i] + ",,,");
        }

        target= number;
        for(int p=1; p<(n*3-1); p++){
            target*=2;
        }
        //cheating();
    }
    //-------------------------------------------------------------------------


    private void cheating() {
        int p = number;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = p;
                p = p + p;
            }
        }
        a[0][0] = number+number;
    }


    //----------------------For showing----------------------------------------
    public void show() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                System.out.print(a[i][j] + "\t");
            System.out.println("");
        }
        System.out.println("");
    }
    //-------------------------------------------------------------------------


    //--------------For Slideing-----------------------------------------------
    public boolean slide(String dir) {
        boolean couldSlide= false;
        switch (dir) {
            case "up":
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++)
                        for (int i = 0; i < n - 1; i++) {
                            if (a[i][j] == 0) {
                                a[i][j] = a[i + 1][j];
                                a[i + 1][j] = 0;
                                couldSlide= true;
                            }
                        }
                }
                break;

            case "down":
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++)
                        for (int i = n - 1; i > 0; i--) {
                            if (a[i][j] == 0) {
                                a[i][j] = a[i - 1][j];
                                a[i - 1][j] = 0;
                                couldSlide= true;
                            }
                        }
                }
                break;

            case "left":
                for (int i = 0; i < n; i++) {
                    for (int k = 0; k < n; k++)
                        for (int j = 0; j < n - 1; j++) {
                            if (a[i][j] == 0) {
                                a[i][j] = a[i][j + 1];
                                a[i][j + 1] = 0;
                                couldSlide= true;
                            }
                        }
                }
                break;

            case "right":
                for (int i = 0; i < n; i++) {
                    for (int k = 0; k < n; k++)
                        for (int j = n - 1; j > 0; j--) {
                            if (a[i][j] == 0) {
                                a[i][j] = a[i][j - 1];
                                a[i][j - 1] = 0;
                                couldSlide= true;
                            }
                        }
                }
                break;

            default:
                break;
        }
        return couldSlide;
    }
    //-------------------------------------------------------------------------


    //-----------------For adding----------------------------------------------
    public boolean adding(String dir) {
        boolean couldAdded= false;
        switch (dir) {
            case "down":
                for (int j = 0; j < n; j++) {
                    for (int i = n - 1; i > 0; i--) {
                        if (a[i][j] == a[i - 1][j]) {
                            score = score + (a[i][j] += a[i][j]);
                            a[i - 1][j] = 0;
                            if (a[i][j] > best) {
                                best = a[i][j];
                                level++;
                            }
                            couldAdded= true;
                        }
                    }
                }

                break;

            case "up":
                for (int j = 0; j < n; j++) {
                    for (int i = 0; i < n - 1; i++) {
                        if (a[i][j] == a[i + 1][j]) {
                            score = score + (a[i][j] += a[i][j]);
                            a[i + 1][j] = 0;
                            if (a[i][j] > best) {
                                best = a[i][j];
                                level++;
                            }
                            couldAdded= true;
                        }
                    }
                }
                break;

            case "left":
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n - 1; j++) {
                        if (a[i][j] == a[i][j + 1]) {
                            score = score + (a[i][j] += a[i][j]);
                            a[i][j + 1] = 0;
                            if (a[i][j] > best) {
                                best = a[i][j];
                                level++;
                            }
                            couldAdded= true;
                        }
                    }
                }
                break;

            case "right":
                for (int i = 0; i < n; i++) {
                    for (int j = n - 1; j > 0; j--) {
                        if (a[i][j] == a[i][j - 1]) {
                            score = score + (a[i][j] += a[i][j]);
                            a[i][j - 1] = 0;
                            if (a[i][j] > best) {
                                best = a[i][j];
                                level++;
                            }
                            couldAdded= true;
                        }
                    }
                }
                break;

            default:

                break;
        }

        return couldAdded;
    }


    //-------------------Generating at random position-------------------------
    public boolean random() {
        int count = 0;
        int b[] = new int[n * n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 0) {
                    b[count] = i * 10 + j;
                    count++;
                }
            }
        if (count == 0)
            return false;

        Random ran = new Random();
        int tval = ran.nextInt(count);
        int rval = b[tval];
        int aa = rval / 10;
        int bb = rval % 10;
        boolean bbb = ran.nextBoolean();
        if (bbb)
            a[aa][bb] = number;
        else
            a[aa][bb] = number * 2;

        return true;
    }







}//classEND

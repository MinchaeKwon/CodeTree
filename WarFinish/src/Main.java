/**
 * 종전
 * 삼성 SW 역량테스트 2019 하반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/war-finish/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 14.
 * 
 * 백준 17779 게리맨더링 2
 * https://www.acmicpc.net/problem/17779
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

    static int n;
    static int[][] map;

    static int result = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());

        int[][] map = new int[n][n];
        int total = 0;

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                total += map[i][j];
            }
        }
        
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                for (int d1 = 1; d1 < n; d1++) {
                    for (int d2 = 1; d2 < n; d2++) {
                        if (x + d1 + d2 >= n || y - d1 < 0 || y + d2 >= n) {
                            continue;
                        }

                        boolean[][] border = new boolean[n][n]; // 경계선 저장

                        for (int i = 0; i <= d1; i++) {
                            border[x + i][y - i] = true;
                            border[x + i + d2][y - i + d2] = true;
                        }

                        for (int i = 0; i <= d2; i++) {
                            border[x + i][y + i] = true;
                            border[x + d1 + i][y - d1 + i] = true;
                        }

                        int[] size = new int[5]; // 각 부족의 인구수 저장

                        // 2번 부족
                        for (int i = 0; i < x + d1; i++) {
                            for (int j = 0; j <= y; j++) {
                                if (border[i][j]) {
                                    break;
                                }

                                size[1] += map[i][j];
                            }
                        }

                        // 3번 부족
                        for (int i = 0; i <= x + d2; i++) {
                            for (int j = n - 1; j > y; j--) {
                                if (border[i][j]) {
                                    break;
                                }

                                size[2] += map[i][j];
                            }
                        }

                        // 4번 부족
                        for (int i = x + d1; i < n; i++) {
                            for (int j = 0; j < y - d1 + d2; j++) {
                                if (border[i][j]) {
                                    break;
                                }

                                size[3] += map[i][j];
                            }
                        }

                        // 5번 부족
                        for (int i = x + d2 + 1; i < n; i++) {
                            for (int j = n - 1; j >= y - d1 + d2; j--) {
                                if (border[i][j]) {
                                    break;
                                }

                                size[4] += map[i][j];
                            }
                        }

                        // 1번 부족은 기울어진 직사각형의 경계와 그 안에 있는 지역 -> 나머지 부족의 인구 수를 빼면됨
                        size[0] = total;

                        for (int i = 1; i < 5; i++) {
                            size[0] -= size[i];
                        }

                        Arrays.sort(size);

                        result = Math.min(result, size[4] - size[0]);
                    }
                }
            }
        }

        System.out.println(result);
    }
}
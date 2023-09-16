/**
 * 이상한 다트 게임
 * 삼성 SW 역량테스트 2019 하반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/odd-dart-game/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 16.
 * 
 * 백준 17822 원판 돌리기
 * https://www.acmicpc.net/problem/17822
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {

    static int n, m, q;
    static int[][] map;

    static ArrayList<int[]> same; // 인접한 수 저장

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());

        map = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 1; j <= m; j++) {
                map[i][j] = Integer.parseInt(st.nextToken()); // 원판에 적힌 수
            }
        }

        while (q-- > 0) {
            st = new StringTokenizer(br.readLine());

            int x = Integer.parseInt(st.nextToken()); // 회원하는 원판의 종류
            int d = Integer.parseInt(st.nextToken()); // 0 시계 방향, 1 반시계 방향
            int k = Integer.parseInt(st.nextToken()); // 회전하는 칸

            rotate(x, d, k); // 회전 시킴

            findSameNum(); // 인접한 수 찾음

            if (same.size() > 0) {
                removeNum(); // 인접한 수가 있는 경우에만 숫자 지움
            } else {
                normalization(); // 없는 경우에는 정규화 시킴
            }
        }

        int result = 0; // q번 회전 시킨 후에 게임판에 남아있는 수의 총합

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                result += map[i][j];
            }
        }

        System.out.println(result);
    }

    // 게임판 돌리기 (원판의 번호가 x의 배수일 경우 d방향으로 k칸 회전시킴)
    private static void rotate(int x, int d, int k) {
        for (int num = x; num <= n; num += x) {
            for (int cnt = 0; cnt < k; cnt++) {
                int tmp;

                if (d == 0) {
                    // 시계 방향으로 회전
                    tmp = map[num][m];

                    for (int i = m; i > 1; i--) {
                        map[num][i] = map[num][i - 1];
                    }

                    map[num][1] = tmp;
                } else {
                    // 반시계 방향으로 회전
                    tmp = map[num][1];

                    for (int i = 1; i < m; i++) {
                        map[num][i] = map[num][i + 1];
                    }

                    map[num][m] = tmp;
                }
            }
        }
    }

    // 인접하면서 숫자가 같은 것을 찾음
    private static void findSameNum() {
        same = new ArrayList<>();

        // 같은 원판에 있는 인접한 수 찾음
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j < m; j++) {
                if (map[i][j] > 0) {
                    // (r, 1)은 (r, m)과 인접
                    if (j == 1 && map[i][j] == map[i][m]) {
                        same.add(new int[] {i, j});
                        same.add(new int[] {i, m});
                    }

                    // (r, j)는 (r, j-1), (r, j+1)과 인접
                    if (map[i][j] == map[i][j + 1]) {
                        same.add(new int[] {i, j});
                        same.add(new int[] {i, j + 1});
                    }
                }
            }
        }

        // 다른 원판에 있는 인접한 수 찾음
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= m; j++) {
                if (map[i][j] > 0) {
                    if (map[i][j] == map[i + 1][j]) {
                        same.add(new int[] {i, j});
                        same.add(new int[] {i + 1, j});
                    }
                }
            }
        }
    }

    // 원판에 있는 숫자 지움
    private static void removeNum() {
        for (int[] num : same) {
            map[num[0]][num[1]] = 0;
        }
    }

    // 원판 전체에 적힌 수의 평균를 구해 정규화 (전체 원판에서 평균보다 큰 수는 1을 빼고, 작은 수는 1을 더해주는 과정)
    private static void normalization() {
        int sum = 0;
        int cnt = 0;

        // 원판에 적힌 숫자들의 합과 개수를 구함
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // 숫자가 있는 경우에만 더하고 개수를 셈
                if (map[i][j] > 0) {
                    sum += map[i][j];
                    cnt++;
                }
            }
        }

        // 원판에 남은 수가 없을 경우에는 정규화를 진행하지 않음
        if (cnt == 0) {
            return;
        }

        int average = sum / cnt; // 전체 원판의 평균을 구함 (평균을 구할 때는 편의상 소숫점 아래의 수는 버림)

        // 평균보다 큰 수는 1을 빼고, 작은 수는 1을 더해줌
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (map[i][j] > 0) {
                    if (map[i][j] > average) {
                        map[i][j]--;
                    } else if (map[i][j] < average) {
                        map[i][j]++;
                    }
                }
            }
        }
    }

}
/**
 * 정육면체 굴리기
 * 삼성 SW 역량테스트 2016 하반기 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/cube-rounding/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 8. 31.
 * 
 * 백준 14499 주사위 굴리기
 * https://www.acmicpc.net/problem/14499
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

    // 동서북남(우좌상하)
    static int[] dx = {0, 0, -1, 1};
    static int[] dy = {1, -1, 0, 0};

    static int n, m, sx, sy, k;
    static int[][] map;
    static int[] direction;

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        sx = Integer.parseInt(st.nextToken());
        sy = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        map = new int[n][m];
        direction = new int[k];

        for (int i = 0; i < n ; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < m; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int[] dice = new int[6]; // 처음 정육면체의 각 면에는 0이 쓰여져 있음

        st = new StringTokenizer(br.readLine());

        while (k-- > 0) {
            int dir = Integer.parseInt(st.nextToken()) - 1;

            int nx = sx + dx[dir];
            int ny = sy + dy[dir];

            if (checkRange(nx, ny)) {
                int[] tmp = Arrays.copyOf(dice, 6);

                // 정육면체 굴리기 - 윗면은 0, 뒷면은 1, 오른쪽면은 2, 왼쪽면은 3, 앞면은 4, 아랫면은 5
                switch (dir) {
                    case 0: // 동쪽
                        dice[0] = tmp[3];
                        dice[2] = tmp[0];
                        dice[3] = tmp[5];
                        dice[5] = tmp[2];
                        break;
                
                    case 1: // 서쪽
                        dice[0] = tmp[2];
                        dice[2] = tmp[5];
                        dice[3] = tmp[0];
                        dice[5] = tmp[3];
                        break;

                    case 2: // 북쪽
                        dice[0] = tmp[4];
                        dice[1] = tmp[0];
                        dice[4] = tmp[5];
                        dice[5] = tmp[1];
                        break;

                    case 3: // 남쪽
                        dice[0] = tmp[1];
                        dice[1] = tmp[5];
                        dice[4] = tmp[0];
                        dice[5] = tmp[4];
                        break;
                }

                if (map[nx][ny] == 0) {
                    // 칸에 쓰여져 있는 수가 0이면, 주사위의 바닥면에 쓰여져있는 수가 칸에 복사됨 (주사위의 숫자는 변하지 X)
                    map[nx][ny] = dice[5];
                } else {
                    // 칸에 쓰여져 있는 수가 0이 아니면 칸에 쓰여져 있는 수가 주사위 바닥면에 복사됨, 해당 칸의 수는 0이 됨
                    dice[5] = map[nx][ny];
                    map[nx][ny] = 0;
                }

                System.out.println(dice[0]);

                sx = nx;
                sy = ny;
            }

        }
        
    }

    private static boolean checkRange(int x, int y) {
        return x>= 0 && x < n && y >= 0 && y < m;
    }
}
/**
 * 자율주행 자동차
 * 삼성 SW 역량테스트 2017 상반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/autonomous-driving/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 1.
 * 
 * 백준 14503 로봇 청소기
 * https://www.acmicpc.net/problem/14503
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    // 북동남서(상우하좌)
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};

    static int n, m;
    static int sx, sy, d;
    static int[][] map; // 도로는 0, 인도는 1

    static int result = 1; // 처음 자동차가 있는 위치도 도로이기 때문에 1부터 시작

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        map = new int[n][m];

        st = new StringTokenizer(br.readLine());

        int sx = Integer.parseInt(st.nextToken());
        int sy = Integer.parseInt(st.nextToken());
        int d = Integer.parseInt(st.nextToken());

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < m; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        moveCar(sx, sy, d);

        System.out.println(result);
    }

    // 자동차 움직이기
    private static void moveCar(int x, int y, int d) {
        map[x][y] = 2; // 2로 도로 방문표시 (자동차가 있는 칸도 도로) / 맵에 2로 도로라는 것을 표시하기 때문에 따로 방문확인은 안해도됨

        // 4방향으로 이동할 수 있는지 확인
        for (int i = 0; i < 4; i++) {
            d = (d + 3) % 4; // 반시계 방향으로 회전 (현재 방향을 기준으로 왼쪽 방향으로 간적이 있는지 보는 것이기 때문에 반시계 방향으로 회전함)

            int nx = x + dx[d];
            int ny = y + dy[d];

            // 도로인 경우 (맵의 가장자리는 인도로 되어있기 때문에 범위 확인 안해도됨)
            if (map[nx][ny] == 0) {
                result++;
                moveCar(nx, ny, d); // 해당 칸으로 이동

                return; // 여기서 종료하지 않으면 밑에서 후진하게 되기 때문에 종료하는 것
            }
        }

        // 후진하기
        int bd = (d + 2) % 4; // 후진 방향
        int bx = x + dx[bd];
        int by = y + dy[bd];

        // 후진하려는 곳이 인도가 아닌 경우 (후진 시에는 방문했던 도로도 재방문 가능하기 때문에 인도가 아닌 경우로 판단)
        if (map[bx][by] != 1) {
            moveCar(bx, by, d); // 해당 칸으로 이동 -> 후진은 원래 방향 그대로 이동하므로 기존 방향으로 재귀 호출
        }
    }

}
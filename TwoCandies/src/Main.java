/**
 * 2개의 사탕
 * 삼성 SW 역량테스트 2015 하반기 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/two-candies/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 8. 25.
 * 
 * 백준 13460 구슬 탈출 2
 * https://www.acmicpc.net/problem/13460
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

class Candy {
    int rx, ry; // 빨간 사탕 위치
    int bx, by; // 파란 사탕 위치
    int move; // 기울인 횟수

    public Candy(int rx, int ry, int bx, int by, int move) {
        this.rx = rx;
        this.ry = ry;
        this.bx = bx;
        this.by = by;
        this.move = move;
    }
}

public class Main {

    // 상하좌우
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    static int N, M;
    static char[][] map; // '.' 빈칸, ‘#' 장애물, ‘B’ 파란색 사탕, ‘R’ 빨간색 사탕, 'O’ 출구

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new char[N][M];
        int rx = 0, ry = 0, bx = 0, by = 0;

        for (int i = 0; i < N; i++) {
            map[i] = br.readLine().toCharArray();

            for (int j = 0; j < M; j++) {
                if (map[i][j] == 'R') {
                    rx = i;
                    ry = j;
                } else if (map[i][j] == 'B') {
                    bx = i;
                    by = j;
                }
            }
        }

        System.out.println(leanBox(new Candy(rx, ry, bx, by, 0)));
    }

    // 상자 기울여서 빨간 사탕 밖으로 빼내기
    private static int leanBox(Candy start) {
        Queue<Candy> q = new LinkedList<>();
        boolean[][][][] visited = new boolean[N][M][N][M]; // 빨강, 파랑 사탕 위치 방문 한번에 확인

        q.add(start);
        visited[start.rx][start.ry][start.bx][start.by] = true;

        while (!q.isEmpty()) {
            Candy cur = q.poll();

            // 10번을 초과할 경우 -1 return
            if (cur.move > 10) {
                return -1;
            }

            // 파란 사탕이 출구로 나오게 되면 더이상 탐색하지 않고 다음 탐색으로 넘어감 (빨간 사탕과 같이 나오게 되면 안되기 때문에 먼저 확인)
            if (map[cur.bx][cur.by] == 'O') {
                continue;
            }

            // 빨간 사탕이 출구로 나오게 되면 이동횟수 return
            if (map[cur.rx][cur.ry] == 'O') {
                return cur.move;
            }

            // 상하좌우로 상자 기울여보기
            for (int i = 0; i < 4; i++) {
                int nbx = cur.bx;
                int nby = cur.by;

                // 파란 사탕이 장애물을 만날 때까지 한 칸씩 이동 (상자의 모든 가장자리는 장애물이기 때문에 범위 확인은 따로 하지 않아도됨)
                while (map[nbx + dx[i]][nby + dy[i]] !='#') {
                    nbx += dx[i];
                    nby += dy[i];

                    // 출구를 만난 경우 탈출
                    if (map[nbx][nby] == 'O') {
                        break;
                    }
                }

                int nrx = cur.rx;
                int nry = cur.ry;

                // 빨간 사탕이 장애물을 만날 때까지 이동
                while (map[nrx + dx[i]][nry + dy[i]] !='#') {
                    nrx += dx[i];
                    nry += dy[i];

                    // 출구를 만난 경우 탈출
                    if (map[nrx][nry] == 'O') {
                        break;
                    }
                }

                // 파란 사탕과 빨간 사탕의 이동 위치가 같아서 서로 만나게 될 경우 이동위치 조정
                // 이동거리가 더 짧은 사탕을 움직이고 다른 사탕을 원래 위치로 되돌림 (이동거리가 짧다는 것은 더 빨리 도착했다는 것이기 때문)
                if (nrx == nbx && nry == nby && map[nrx][nry] != 'O') {
                    int red = Math.abs(nrx - cur.rx) + Math.abs(nry - cur.ry); // 빨간 사탕 이동거리
                    int blue = Math.abs(nbx - cur.bx) + Math.abs(nby - cur.by); // 파란 사탕 이동거리

                    if (red > blue) {
                        // 빨간 구슬 이동거리가 더 길 경우 이전 위치로 되돌림
                        nrx -= dx[i];
                        nry -= dy[i];
                    } else {
                        nbx -= dx[i];
                        nby -= dy[i];
                    }
                }

                // 두 개의 사탕의 이동 위치를 처음 방문하는 곳인 경우
                if (!visited[nrx][nry][nbx][nby]) {
                    q.add(new Candy(nrx, nry, nbx, nby, cur.move + 1));
                    visited[nrx][nry][nbx][nby] = true;
                }
            }
        }

        return -1;
    }

}
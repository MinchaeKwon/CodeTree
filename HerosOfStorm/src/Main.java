/**
 * 시공의 돌풍
 * 삼성 SW 역량테스트 2019 상반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/heros-of-storm/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 11.
 * 
 * 백준 17144 미세먼지 안녕!
 * https://www.acmicpc.net/problem/17144
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

class Dust {
    int x;
    int y;
    int size;

    public Dust(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }
}

public class Main {

    // 상하좌우
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    static int n, m;
    static int[][] map; // 시공의 돌풍이 있는 곳 -1(항상 맨 왼쪽에 위치)

    static int[] storm = new int[2]; // 돌풍이 있는 위치 (행의 위치만 저장)
    static Queue<Dust> q;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        int t = Integer.parseInt(st.nextToken());

        map = new int[n][m];

        int idx = 0;

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < m; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());

                if (map[i][j] == -1) {
                    storm[idx++] = i;
                }
            }
        }

        // t초만큼 반복
        while (t-- > 0) {
            checkDust();
            spreadDust();
            cleanStorm();
        }

        System.out.println(getDustCnt()); // 남아있는 먼지의 양 출력
    }

    // 1. 먼지가 있는 곳 확인
    private static void checkDust() {
        q = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (map[i][j] > 0) {
                    q.add(new Dust(i, j, map[i][j]));
                }
            }
        }
    }

    // 2. 먼지 확산
    private static void spreadDust() {
        while (!q.isEmpty()) {
            Dust cur = q.poll();

            int cnt = 0; // 먼지가 확산된 칸의 개수
            
            for (int i = 0; i < 4; i++) {
                int nx = cur.x + dx[i];
                int ny = cur.y + dy[i];

                // 범위 벗어나지 않고, 돌풍이 없는 곳인 경우
                if (checkRange(nx, ny) && map[nx][ny] != -1) {
                    map[nx][ny] += cur.size / 5; // 원래 칸의 먼지 양을 5로 나눈 값을 더함
                    cnt++;
                }
            }

            map[cur.x][cur.y] -= (cur.size / 5) * cnt; // 원래 칸의 먼지의 양은 확산된 먼지만큼 감소시킴
        }
    }

    // 3. 시공의 돌풍으로 청소
    private static void cleanStorm() {
        int top = storm[0]; // 돌풍 위쪽
        int down = storm[1]; // 돌풍 아래쪽

        // 바람이 불면 먼지가 바람의 방향대로 모두 한 칸씩 이동

        // 위쪽은 반시계 방향으로 바람을 일으킴

        // 1) 아래로 이동
        for (int i = top - 1; i > 0; i--) {
            map[i][0] = map[i - 1][0];
        }

        // 2) 왼쪽으로 이동
        for (int i = 0; i < m - 1; i++) {
            map[0][i] = map[0][i + 1];
        }

        // 3) 위로 이동
        for (int i = 0; i < top; i++) {
            map[i][m - 1] = map[i + 1][m - 1];
        }

        // 4) 오른쪽으로 이동
        for (int i = m - 1; i > 1; i--) {
            map[top][i] = map[top][i - 1];
        }

        map[top][1] = 0; // 돌풍에서 나오는 바람은 먼지가 없는 바람이기 때문에 바로 옆에는 0을 넣어줌

        // 아래쪽은 시계 방향으로 바람을 일으킴

        // 1) 위로 이동
        for (int i = down + 1; i < n - 1; i++) {
            map[i][0] = map[i + 1][0];
        }

        // 2) 왼쪽으로 이동
        for (int i = 0; i < m - 1; i++) {
            map[n - 1][i] = map[n - 1][i + 1];
        }

        // 3) 아래로 이동
        for (int i = n - 1; i > down; i--) {
            map[i][m - 1] = map[i - 1][m - 1];
        }

        // 4) 오른쪽로 이동
        for (int i = m - 1; i > 1; i--) {
            map[down][i] = map[down][i - 1];
        }

        map[down][1] = 0; // 돌풍에서 나오는 바람은 먼지가 없는 바람이기 때문에 바로 옆에는 0을 넣어줌
    }

    // 남아있는 먼지의 양 구하기
    private static int getDustCnt() {
        int cnt = 0;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (map[i][j] > 0) {
                    cnt += map[i][j];
                }
            }
        }

        return cnt;
    }

    private static boolean checkRange(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < m;
    }
}
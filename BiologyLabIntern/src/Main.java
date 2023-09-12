/**
 * 생명과학부 랩 인턴
 * 삼성 SW 역량테스트 2019 상반기 오전 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/biology-lab-intern/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 12.
 * 
 * 백준 17143 낚시왕
 * https://www.acmicpc.net/problem/17143
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Mold {
    int x;
    int y;
    int s; // 1초동안 곰팡이가 움직이는 거리
    int d; // 이동 방향 (0 상, 1 하, 2 우, 3 좌)
    int b; // 곰팡이의 크기

    public Mold(int x, int y, int s, int d, int b) {
        this.x = x;
        this.y = y;
        this.s = s;
        this.d = d;
        this.b = b;
    }
}

public class Main {

    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, 1, -1};

    static int n, m, k;
    static Mold[][] map;

    static int result = 0; // 인턴이 채취한 곰팡이 크기의 총 합

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        map = new Mold[n][m];

        while (k-- > 0) {
            st = new StringTokenizer(br.readLine());

            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int s = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken()) - 1;
            int b = Integer.parseInt(st.nextToken());

            map[x][y] = new Mold(x, y, s, d, b);
        }

        // 1. 승용이가 첫 번째 열부터 탐색 시작
        for (int y = 0; y < m; y++) {
            findNearbyMold(y);
            moveMold();
        }

        System.out.println(result);
    }

    // 2. 특정 열의 위에서 아래로 내려가며 탐색 -> 행에서 가까이에 있는 곰팡이 찾음
    private static void findNearbyMold(int y) {
        for (int x = 0; x < n; x++) {
            // 승용이와 제일 가까운 곰팡이 찾은 경우
            if (map[x][y] != null) {
                result += map[x][y].b; // 곰팡이 크기 더함
                map[x][y] = null; // 해당 칸은 빈칸이 됨

                return;
            }
        }
    }

    // 3. 곰팡이가 이동함
    private static void moveMold() {
        Mold[][] copy = new Mold[n][m]; // 곰팡이가 모두 이동하고 크기를 비교한 후에 제거되거나 살아남기 때문에 원본 배열 복사해서 사용

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                Mold cur = map[x][y];

                // 곰팡이가 없는 칸일 경우 다음 탐색으로 넘어감
                if (cur == null) {
                    continue;
                }

                // 입력으로 주어진 방향과 속력으로 이동
                int s = cur.s;
                s %= (cur.d < 2 ? n - 1 : m - 1) * 2;
                
                for (int i = 0; i < s; i++) {
                    int nx = cur.x + dx[cur.d];
                    int ny = cur.y + dy[cur.d];

                    // 가장자리에 도달하면 방향을 바꾸고 속력을 유지한 채로 나머지를 이동함
                    if (!checkRange(nx, ny)) {
                        // 이전 칸으로 돌아감
                        cur.x -= dx[cur.d];
                        cur.y -= dy[cur.d];

                        cur.d = cur.d % 2 == 0 ? cur.d + 1 : cur.d - 1; // 반대 방향으로 바꾸기
                        
                        continue;
                    }

                    // 곰팡이 위치 갱신
                    cur.x = nx;
                    cur.y = ny;
                }

                // 곰팡이가 없거나, 맵에 이미 있는 곰팡이 크기보다 큰 경우
                if (copy[cur.x][cur.y] == null || copy[cur.x][cur.y].b < cur.b) {
                    copy[cur.x][cur.y] = cur; // 해당 곰팡이 넣어줌
                }
            }
        }

        map = copy; // 원본 배열에 모든 곰팡이가 이동한 맵을 복사
    }

    private static boolean checkRange(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < m;
    }

}
/**
 * 이상한 윷놀이
 * 삼성 SW 역량테스트 2019 하반기 오전 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/odd-woodstick-game/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 15.
 * 
 * 백준 17837 새로운 게임 2
 * https://www.acmicpc.net/problem/17837
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class Main {

    static class Horse {
        int x;
        int y;
        int d;

        public Horse(int x, int y, int d) {
            this.x = x;
            this.y = y;
            this.d = d;
        }
    }

    // 우좌상하
    static int[] dx = {0, 0, -1, 1};
    static int[] dy = {1, -1, 0, 0};

    static int n, k;
    static int[][] color; // 0은 흰색 판, 1은 빨간색 판, 2는 파란색 판

    static ArrayList<Integer>[][] map; // 한칸에 여러 말이 있을 수 있기 때문에 2차원 리스트 배열 사용
    static Horse[] horses;

    static int answer = 0;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        color = new int[n][n];
        map = new ArrayList[n][n];
        horses = new Horse[k];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < n; j++) {
                color[i][j] = Integer.parseInt(st.nextToken());
                map[i][j] = new ArrayList<>();
            }
        }

        for (int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine());

            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int d = Integer.parseInt(st.nextToken()) - 1;

            horses[i] = new Horse(x, y, d);
            map[x][y].add(i);
        }

        play();

        System.out.println(answer > 1000 ? -1 : answer);
    }

    // 윷놀이 진행
    private static void play() {
        while (answer++ < 1000) {
            for (int i = 0; i < k; i++) {
                Horse horse = horses[i]; // 현재 이동시킬 말
                ArrayList<Integer> above = new ArrayList<>(); // 특정 말 위에 있는 말들을 저장

                int x = horse.x;
                int y = horse.y;

                int start = 0;

                // 현재 이동시키려는 말이 해당 칸에서 몇번째에 위치하는지 확인
                for (int num = 0; num < map[x][y].size(); num++) {
                    if (i == map[x][y].get(num)) {
                        start = num;
                        break;
                    }
                }

                // 현재 이동시킬 말을 포함해서 위에 있는 말들 저장
                for (int num = start; num < map[x][y].size(); num++) {
                    above.add(map[x][y].get(num));
                }

                int nx = x + dx[horse.d];
                int ny = y + dy[horse.d];

                // 범위를 벗어나거나 파란색 칸일 경우
                if (nx < 0 || nx >= n || ny < 0 || ny >= n || color[nx][ny] == 2) {
                    // 원위치
                    nx -= dx[horse.d];
                    ny -= dy[horse.d];

                    // 방향 반대로
                    int dir = horse.d % 2 == 0 ? horse.d + 1 : horse.d - 1;

                    // 반대 방향으로 전환
                    nx += dx[dir];
                    ny += dy[dir];

                    horse.d = dir; // 방향 바꿔줌

                    // 방향 전환한 뒤 이동하려는 칸도 범위를 벗어나거나 파란색이라면 이동하지 않고 가만히 있음
                    if (nx < 0 || nx >= n || ny < 0 || ny >= n || color[nx][ny] == 2) {
                        continue;
                    } else {
                        moveHorse(color[nx][ny], nx, ny, above);
                    }
                } else {
                    moveHorse(color[nx][ny], nx, ny, above);
                }

                // 말이 4개 이상 겹쳐지는 경우 그 즉시 게임 종료
                if (map[nx][ny].size() >= 4) {
                    return;
                }

                // 이동한 후에 원래 위치에 있는 말 삭제시킴 -> 맨 위에 있는 말부터 이동하려는 말까지 삭제
                for (int num = map[x][y].size() - 1; num >= start; num--) {
                    map[x][y].remove(num);
                }

            }
        }
    }

    // 말 이동 (흰색, 빨간색 칸일 경우 말 이동시킴)
    private static void moveHorse(int type, int x, int y, ArrayList<Integer> above) {
        if (type == 0) { // 흰색인 경우
            for (Integer num : above) {
                // 차례대로 이동하려는 칸으로 말을 이동시킴
                map[x][y].add(num);

                horses[num].x = x;
                horses[num].y = y;
            }
        } else if (type == 1) { // 빨간색인 경우
            // 순서를 바꿔서 이동하려는 칸으로 말을 이동시킴
            for (int i = above.size() - 1; i >= 0; i--) {
                map[x][y].add(above.get(i));

                horses[above.get(i)].x = x;
                horses[above.get(i)].y = y;
            }
        }
    }
    
}
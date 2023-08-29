/**
 * 2048 게임
 * 삼성 SW 역량테스트 2016 하반기 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/2048-game/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 8. 29.
 * 
 * 백준 12100 2048 (Easy)
 * https://www.acmicpc.net/problem/12100
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

    // 상하좌우
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    static int n;
    static int[][] map;
    
    static int[][] copy;
    static boolean[][] visited;
    static int[] direction = new int[5]; // 블록 5번 이동시킬 때의 방향 저장

    static int result = 0;

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        n = Integer.parseInt(br.readLine());

        map = new int[n][n];

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        backtracking(0);

        System.out.println(result);
    }

    // 백트래킹 이용해서 블록 5번 이동시킬 때의 방향 구함
    private static void backtracking(int depth) {
        if (depth == 5) {
            checkBlock();
            return;
        }

        for (int i = 0; i < 4; i++) {
            direction[depth] = i;
            backtracking(depth + 1);
        }
    }

    // 블록 5번 이동
    private static void checkBlock() {
        // 배열 복사해서 사용 -> 백트래킹을 사용하기 때문에 원본 배열은 건들지 않아야함
        copy = new int[n][n];

        for (int i = 0; i < n; i++) {
            copy[i] = Arrays.copyOf(map[i], n);
        }

        for (int d = 0; d < 5; d++) {
            int dir = direction[d]; // 상 0, 하 1, 좌 2, 우 3
            visited = new boolean[n][n]; // 블록이 이미 합쳐졌는지 확인 -> 한번 이동할때마다 초기화

            // 상/하는 특정 열에 행만 변화시키면서 확인, 좌/우는 특정 행에 열을 변화시키면서 확인
            switch (dir) {
                case 0: // 상
                    for (int j = 0; j < n; j++) {
                        for (int i = 0; i < n; i++) {
                            moveBlock(i, j, dir);
                        }
                    }
                    break;

                case 1: // 하 (아래쪽부터 확인해야 하므로 n-1부터 시작)
                    for (int j = n - 1; j >= 0; j--) {
                        for (int i = 0; i < n; i++) {
                            moveBlock(i, j, dir);
                        }
                    }
                    break;

                case 2: // 좌
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            moveBlock(i, j, dir);
                        }
                    }
                    break;

                case 3: // 우
                    for (int i = n - 1; i >= 0; i--) {
                        for (int j = 0; j < n; j++) {
                            moveBlock(i, j, dir);
                        }
                    }
                    break;
            }
        }

        getMaxScore(); // 최댓값 갱신
    }

    // 특정 방향으로 블록 이동
    private static void moveBlock(int x, int y, int dir) {
        // 빈칸일 경우 탐색하지 않음
        if (copy[x][y] == 0) {
            return;
        }

        // 해당 방향으로 최대한 이동시키기
        while (true) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            // 범위를 벗어나거나 블록이 이미 합쳐진 경우 종료
            if (!checkRange(nx, ny) || visited[nx][ny]) {
                break;
            }

            if (copy[x][y] == copy[nx][ny]) { // 다음 블럭과 숫자가 같을 경우
                visited[nx][ny] = true; // 방문처리

                copy[nx][ny] *= 2; // 숫자 합치기 -> 같은 숫자가 합쳐지니까 *2 해줌
                copy[x][y] = 0;

                break;
            } else if (copy[nx][ny] == 0) { // 다음 칸이 빈칸일 경우
                copy[nx][ny] = copy[x][y]; // 블록을 해당 방향으로 이동시킴
                copy[x][y] = 0;

                x = nx;
                y = ny;
            } else {
                break; // 숫자가 다른데 빈칸이 아닌 경우에는 더이상 이동시킬 수 없으므로 종료
            }
        }
    }

    // 가장 큰 블록의 수 구하기
    private static void getMaxScore() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result = Math.max(result, copy[i][j]);
            }
        }
    }

    private static boolean checkRange(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }
}
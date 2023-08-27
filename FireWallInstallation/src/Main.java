/**
 * 방화벽 설치하기
 * 삼성 SW 역량테스트 2017 상반기 오후 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/firewall-installation/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 8. 27.
 * 
 * 백준 14502 연구소
 * https://www.acmicpc.net/problem/14502
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    // 상하좌우
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    static int n, m;
    static int[][] map; // 빈칸 0, 방화벽 1, 불 2

    static int result = 0;

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        map = new int[n][m];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < m; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        install(0);

        System.out.println(result);
    }

    // 방화벽 3개 설치하기
    private static void install(int depth) {
        if (depth == 3) {
            spreadFire(); // 불 퍼뜨리기
            return;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // 빈칸인 경우 방화벽 설치
                if (map[i][j] == 0) {
                    map[i][j] = 1;
                    install(depth + 1);
                    map[i][j] = 0; // 원복
                }
            }
        }
    }

    // 불을 퍼뜨림 -> 방화벽 3개 설치하는 경우의 수가 여러 개이기 때문에 원본 맵을 건드리면 안됨 -> 배열 복사해서 사용
    private static void spreadFire() {
        Queue<int[]> fq = new LinkedList<>(); // 불이 있는 위치 저장
        int[][] fire = new int[n][m]; // 원본맵 복사한 배열
    
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (map[i][j] == 2) {
                    fq.add(new int[] {i, j});
                }

                fire[i][j] = map[i][j];
            }
        }

        while (!fq.isEmpty()) {
            int[] cur = fq.poll();

            for (int i = 0; i < 4; i++) {
                int nx = cur[0] + dx[i];
                int ny = cur[1] + dy[i];

                // 범위를 벗어나지 않고, 빈칸인 경우 불을 퍼뜨림 (불을 퍼뜨리면 해당 칸의 값에 2가 들어가게 되므로 따로 방문처리는 안해도됨)
                if (checkRange(nx, ny) && fire[nx][ny] == 0) {
                    fire[nx][ny] = 2;
                    fq.add(new int[] {nx, ny});
                }
            }
        }

        getSafezone(fire); // 안전한 영역 최댓값 구하기
    }

    // 안전영역 크기 구하고 최댓값 갱신
    private static void getSafezone(int[][] arr) {
        int cnt = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // 빈칸인 경우가 안전한 곳임
                if (arr[i][j] == 0) {
                    cnt++;
                }
            }
        }

        result = Math.max(result, cnt);
    }

    // 범위 확인
    private static boolean checkRange(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < m;
    }

}
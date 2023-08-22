/**
 * 테트리스 블럭 안의 합 최대화 하기
 * 삼성 SW 역량테스트 2017 상반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/max-sum-of-tetris-block/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 8. 22.
 * 
 * 백준 14500 테트로미노
 * https://www.acmicpc.net/problem/14500
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static int[] dx = { -1, 1, 0, 0 };
    static int[] dy = { 0, 0, -1, 1 };

    static int n, m;
    static int[][] map;
    static boolean[][] visited;

    static int result = 0;

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        map = new int[n][m];
        visited = new boolean[n][m];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < m; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 모든 칸 다 확인
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                visited[i][j] = true;
                calculateSum(i, j, map[i][j], 1);
                visited[i][j] = false;
            }
        }

        System.out.println(result);
    }

    private static void calculateSum(int x, int y, int sum, int depth) {
        // 4칸을 다 방문한 경우
        if (depth == 4) {
            result = Math.max(result, sum);
            return;
        }

        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (checkRange(nx, ny) && !visited[nx][ny]) {
                // 'ㅏ' 모양 때문에 두번째까지 탐색한 경우 현재 칸에서 한 번 더 탐색해야함 -> dfs 한 번 더 실행
                if (depth == 2) {
                    visited[nx][ny] = true;
                    calculateSum(x, y, sum + map[nx][ny], depth + 1);
                    visited[nx][ny] = false;
                }

                visited[nx][ny] = true;
                calculateSum(nx, ny, sum + map[nx][ny], depth + 1);
                visited[nx][ny] = false;
            }
        }
    }

    private static boolean checkRange(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < m;
    }
}
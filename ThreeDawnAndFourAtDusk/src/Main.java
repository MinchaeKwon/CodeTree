/**
 * 조삼모사
 * 삼성 SW 역량테스트 2017 하반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/three-at-dawn-and-four-at-dusk/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 2.
 * 
 * 백준 14889 스타트와 링크
 * https://www.acmicpc.net/problem/14889
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static int n;
    static int[][] map;
    static boolean[] visited;
    static int result = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        n = Integer.parseInt(br.readLine());

        map = new int[n][n];
        visited = new boolean[n];
        
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        backtracking(0, 0);

        System.out.println(result);
    }

    private static void backtracking(int idx, int depth) {
        // 아침과 저녁으로 n/2씩 나누어 처리하기 때문에 n/2개를 뽑으면 업무 강도 차이 구함
        if (depth == n / 2) {
            result = Math.min(result, getDifference()); // 최솟값 갱신
            return;
        }

        // 1. 첫 번째 방법
        // for (int i = idx; i < n; i++) {
        //     visited[i] = true;
        //     backtracking(i + 1, depth + 1);
        //     visited[i] = false; // 원복
        // }


        // 2. 두 번째 방법

        // n/2개로 나누어지지 못한 경우에는 종료
        if (idx == n) {
            return;
        }

        backtracking(idx + 1, depth); // 업무를 저녁에 하는 경우

        // 업무를 아침에 하는 경우
        visited[idx] = true;
        backtracking(idx + 1, depth + 1);
        visited[idx] = false;
    }

    // 업무 강도 차이 구하기
    private static int getDifference() {
        int dawn = 0;
        int dusk = 0;

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (visited[i] && visited[j]) {
                    dawn += map[i][j] + map[j][i];
                } else if (!visited[i] && !visited[j]) {
                    dusk += map[i][j] + map[j][i];
                }
            }
        }

        return Math.abs(dawn - dusk);
    }
}
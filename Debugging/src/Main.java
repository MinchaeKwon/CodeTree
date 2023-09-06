/**
 * 디버깅
 * 삼성 SW 역량테스트 2018 상반기 오전 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/debugging/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 6.
 * 
 * 백준 15684 사다리 조작
 * https://www.acmicpc.net/problem/15684
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static int n, m, h;
    static int[][] map;
    
    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        h = Integer.parseInt(st.nextToken());

        map = new int[h][n];

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());

            int a = Integer.parseInt(st.nextToken()) - 1; // 취약지점
            int b = Integer.parseInt(st.nextToken()) - 1; // 메모리 유실이 일어난 지점

            // b번째 고객에서 (b+1)번째 고객에게로 메모리 유실이 일어남
            map[a][b] = 1;
            map[a][b + 1] = 2;
        }

        // 필요한 선의 개수가 3보다 크다면 -1을 출력해야 되기 때문에 0부터 3까지 반복
        for (int i = 0; i <= 3; i++) {
            backtracking(0, 0, i);
        }

        System.out.println(-1);
    }

    private static void backtracking(int idx, int depth, int r) { // 인덱스, 뽑은 개수, 뽑아야 하는 개수
        if (depth == r) {
            // 뽑아야 하는 개수만큼 선을 다 추가한 경우 i번 줄의 결과가 i번으로 가는지 확인
            if (checkLine()) {
                System.out.println(r); // 선의 개수 최솟값 출력
                System.exit(0); // 선의 개수를 하나씩 늘려가면서 확인하기 때문에 처음 정답이 나오는 경우가 최소값이기 때문에 시스템 종료
            }

            return;
        }

        for (int i = idx; i < h; i++) {
            for (int j = 0; j < n - 1; j++) {
                // 선을 연결할 수 있는 경우
                if (map[i][j] == 0 && map[i][j + 1] == 0) {
                    map[i][j] = 1;
                    map[i][j + 1] = 2;

                    backtracking(idx + 1, depth + 1, r);

                    // 원복
                    map[i][j] = 0;
                    map[i][j + 1] = 0;
                }
            }
        }
    }

    // i번 줄의 결과가 i번으로 가는지 확인
    private static boolean checkLine() {
        for (int j = 0; j < n; j++) { // 열 확인
            int ny = j;

            for (int i = 0; i < h; i++) { // 행 확인
                if (map[i][ny] == 1) { // 1인 경우 열 증가 -> 오른쪽으로 이동
                    ny++;
                } else if (map[i][ny] == 2) { // 2인 경우 열 감소 -> 왼쪽으로 이동
                    ny--;
                }
            }

            // 각각의 행을 확인했는데 열이 같지 않은 경우 i번 줄의 결과가 i번으로 갈 수 없다는 것
            if (ny != j) {
                return false;
            }
        }

        return true;
    }
}
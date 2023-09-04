/**
 * 돌아가는 팔각 의자
 * 삼성 SW 역량테스트 2017 하반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/rounding-eight-angle/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 4.
 * 
 * 백준 14891 톱니바퀴
 * https://www.acmicpc.net/problem/14891
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static char[][] chair = new char[4][8];
    static int[] dir; // 각 의자들이 회전할 방향 저장

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 0; i < 4; i++) {
            chair[i] = br.readLine().toCharArray();
        }

        int k = Integer.parseInt(br.readLine());

        while (k-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            int n = Integer.parseInt(st.nextToken()) - 1; // 회전시킬 팔각의자 번호
            int d = Integer.parseInt(st.nextToken()); // 방향 (1 시계방향, -1 반시계방향)

            dir = new int[4];

            checkDir(n, d);
            rotate();
        }

        int result = 0;

        for (int i = 0; i < 4; i++) {
            // 착석한 경우에만 더함 (착석하지 않으면 0이기 때문에 더할 필요 없음)
            if (chair[i][0] == '1') {
                result += Math.pow(2, i);
            }
        }

        System.out.println(result);
    }

    // 회전시킬 방향 확인 (재귀)
    private static void checkDir(int n, int d) {
        dir[n] = d;

        int prev = n - 1;

        // 범위를 벗어나지 않고, 방향이 정해지지 않았으며 지역이 다를 경우
        if (prev >= 0 && dir[prev] == 0 && chair[n][6] != chair[prev][2]) {
            checkDir(prev, d * -1); // 재귀 호출해서 의자를 회전시킬 방향 찾음
        }

        int next = n + 1;

        if (next <= 3 && dir[next] == 0 && chair[n][2] != chair[next][6]) {
            checkDir(next, d * -1);
        }
    }

    // 팔각의자 회전시키기
    private static void rotate() {
        // 의자 개수만큼 반복
        for (int i = 0; i < 4; i++) {
            // 방향이 있는 경우
            if (dir[i] != 0) {
                char[] tmp = new char[8];

                for (int j = 0; j < 8; j++) {
                    int idx = j + dir[i];

                    if (idx == 8) {
                        idx = 0; // 시계방향으로 회전할 경우 7번째에 있던게 0번째로 가야함
                    } else if (idx == -1) {
                        idx = 7; // 반시계방향으로 회전할 경우 0번째에 있던게 7번째로 가야함
                    }

                    tmp[idx] = chair[i][j];
                }

                chair[i] = tmp;
            }
        }
    }
}
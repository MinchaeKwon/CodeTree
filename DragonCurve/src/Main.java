/**
 * 드래곤 커브
 * 삼성 SW 역량테스트 2018 상반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/dragon-curve/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 7.
 * 
 * 백준 15685 드래곤 커브
 * https://www.acmicpc.net/problem/15685
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {

    // 우상좌하
    static int[] dx = {0, -1, 0, 1};
    static int[] dy = {1, 0, -1, 0};

    static boolean[][] map = new boolean[101][101]; // 드래곤 커브가 있는 경우에 true, 아니면 false

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        int n = Integer.parseInt(br.readLine());

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            int g = Integer.parseInt(st.nextToken());

            drawDragonCurve(x, y, d, g);
        }

        System.out.println(getSquareCnt());
    }

    // 드래곤 커브 그리기
    private static void drawDragonCurve(int x, int y, int d, int g) {
        ArrayList<Integer> dirList = new ArrayList<>(); // 각 차수의 방향 저장
        dirList.add(d);

        // 차수만큼 반복 -> 마지막 세대부터 시작하기 때문에 반시계방향으로 회전하면서 방향 추가
        while (g-- > 0) {
            for (int i = dirList.size() - 1; i >= 0; i--) {
                int nd = (dirList.get(i) + 1) % 4; // 반시계방향 회전
                dirList.add(nd);
            }
        }

        // 드래곤 커브 그리기
        map[x][y] = true;

        for (int dir : dirList) {
            x += dx[dir];
            y += dy[dir];

            map[x][y] = true;
        }
    }

    // 꼭짓점이 모두 드래곤 커브의 일부인 크기가 1인 정사각형의 개수 세기
    private static int getSquareCnt() {
        int cnt = 0;

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                // 현재 점에서 4개의 꼭짓점에 드래곤 커브가 있는 경우
                if (map[i][j] && map[i][j + 1] && map[i + 1][j] && map[i + 1][j + 1]) {
                    cnt++;
                }
            }
        }

        return cnt;
    }
}
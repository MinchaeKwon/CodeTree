/**
 * 이상한 체스
 * 삼성 SW 역량테스트 2018 상반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/rounding-eight-angle/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 5.
 * 
 * 백준 15683 감시
 * https://www.acmicpc.net/problem/15683
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

class Chess {
    int x;
    int y;
    int num;

    public Chess(int x, int y, int num) {
        this.x = x;
        this.y = y;
        this.num = num;
    }
}

public class Main {

    // 상우하좌
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, -1, 0, 1};

    static int n, m;
    static int[][] map, copy;

    static ArrayList<Chess> horseList = new ArrayList<>(); // 말의 위치 저장
    static int[] dir; // 각 말의 이동방향 저장

    static int result = Integer.MAX_VALUE;

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

                // 체스 말인 경우
                if (map[i][j] > 0 && map[i][j] < 6) {
                    horseList.add(new Chess(i, j, map[i][j]));
                }
            }
        }

        dir = new int[horseList.size()];
        check(0);

        System.out.println(result);
    }

    // 말이 이동하는 방향 정하기 -> 순열 사용
    private static void check(int depth) {
        if (depth == horseList.size()) {
            // 원본 배열 복사해서 사용
            copy = new int[n][m];

            for (int i = 0; i < n; i++) {
                copy[i] = Arrays.copyOf(map[i], m);
            }

            // 정해진 방향으로 말 이동시켜봄
            for (int i = 0; i < horseList.size(); i++) {
                moveHorse(horseList.get(i), dir[i]);
            }

            result = Math.min(result, getEmptyCnt()); // 이동할 수 없는 영역 넓이 최솟값 갱신

            return;
        }

        // 각 말의 이동방향 정함 (4개의 방향 중에서 말의 개수만큼 순서대로 뽑아 모든 방향의 경우를 봄)
        for (int i = 0; i < 4; i++)  {
            dir[depth] = i;
            check(depth + 1);
        }
    }

    // 말이 이동할 수 있는 공간 확인
    private static void moveHorse(Chess horse, int d) {
        switch (horse.num) {
            case 1:
                moveDir(horse, d);
                break;
        
            case 2:
                if (d == 0 || d == 2) {
                    moveDir(horse, 0);
                    moveDir(horse, 2);
                } else {
                    moveDir(horse, 1);
                    moveDir(horse, 3);
                }
                break;

            case 3:
                if (d == 3) { // 방향이 서쪽일 경우 (좌 상) 확인
                    moveDir(horse, 3);
                    moveDir(horse, 0);
                } else { // 나머지는 현재 방향과 그 다음 방향을 보면 됨
                    moveDir(horse, d);
                    moveDir(horse, d + 1);
                }
                break;

            case 4:
                if (d == 0) { // 우 좌 상
                    moveDir(horse, 3);
                    moveDir(horse, 0);
                    moveDir(horse, 1);
                } else if (d == 1) { // 하 우 상
                    moveDir(horse, 2);
                    moveDir(horse, 1);
                    moveDir(horse, 0);
                } else if (d == 2) { // 좌 하 우
                    moveDir(horse, 3);
                    moveDir(horse, 2);
                    moveDir(horse, 1);
                } else if (d == 3) { // 상 좌 하
                    moveDir(horse, 0);
                    moveDir(horse, 3);
                    moveDir(horse, 2);
                }
                break;

            case 5:
                // 상하우좌 다 확인
                moveDir(horse, 0);
                moveDir(horse, 1);
                moveDir(horse, 2);
                moveDir(horse, 3);
                break;
        }


    }

    // 특정 방향으로 말 이동시키기
    private static void moveDir(Chess horse, int d) {
        int nx = horse.x + dx[d];
        int ny = horse.y + dy[d];

        // 범위를 벗어나지 않고 상대편 말을 만나지 않을 때까지 이동
        while (checkRange(nx, ny) && copy[nx][ny] != 6) {
            // 빈칸인 경우 말이 이동할 수 있으므로 7로 표시
            if (copy[nx][ny] == 0) {
                copy[nx][ny] = 7;
            }

            nx += dx[d];
            ny += dy[d];
        }
    }

    // 자신의 말을 이용해서 갈 수 없는 체스판의 영역 넓이 구하기
    private static int getEmptyCnt() {
        int cnt = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (copy[i][j] == 0) {
                    cnt++;
                }
            }
        }

        return cnt;
    }

    private static boolean checkRange(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < m;
    }
}
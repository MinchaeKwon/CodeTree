/**
 * 바이러스 실험
 * 삼성 SW 역량테스트 2018 하반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/virus-experiment/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 9.
 * 
 * 백준 16235 나무 재테크
 * https://www.acmicpc.net/problem/16235
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static class Virus implements Comparable<Virus> {
        int x;
        int y;
        int age;

        public Virus(int x, int y, int age) {
            this.x = x;
            this.y = y;
            this.age = age;
        }

        @Override
        public int compareTo(Virus o) {
            return this.age - o.age; // 나이를 기준으로 오름차순 정렬
        }
    }

    // 상, 하, 좌, 우, 왼쪽 위, 왼쪽 아래, 오른쪽 위, 오른쪽 아래
    static int[] dx = {-1, 1, 0, 0, -1, 1, -1, 1};
    static int[] dy = {0, 0, -1, 1, -1, -1, 1, 1};

    static int n, m;
    static int[][] add; // 추가되는 양분의 양
    static int[][] map; // 양분 저장

    static PriorityQueue<Virus> virus = new PriorityQueue<>(); // 살이있는 바이러스 저장 (나이가 어린 바이러스부터 양분을 섭취하기 때문에 우선순위 큐 사용)
    static Queue<Virus> dead = new LinkedList<>(); // 양분 섭취 못하고 죽은 바이러스 저장

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken()); // 총 사이클의 수

        add = new int[n][n];
        map = new int[n][n];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < n; j++) {
                add[i][j] = Integer.parseInt(st.nextToken());
                map[i][j] = 5; // 초기에는 각 칸에 5만큼의 양분이 들어있음
            }
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());

            int r = Integer.parseInt(st.nextToken()) - 1;
            int c = Integer.parseInt(st.nextToken()) - 1;
            int age = Integer.parseInt(st.nextToken());

            virus.add(new Virus(r, c, age));
        }

        while (k-- > 0) {
            eatNutrient();
            changeNutrient();
            spreadVirus();
            addNutrient();
        }

        // k 사이클 이후에 살아남은 바이러스의 수 출력
        System.out.println(virus.size());
    }

    // 1. 각각의 바이러스가 해당 칸의 양분 섭취 -> 나이가 어린 바이러스부터 섭취, 만약 본인 나이만큼 먹지 못하면 즉시 죽음
    private static void eatNutrient() {
        Queue<Virus> alive = new LinkedList<>(); // 살아남은 바이러스

        while (!virus.isEmpty()) {
            Virus cur = virus.poll();

            if (map[cur.x][cur.y] >= cur.age) {
                map[cur.x][cur.y] -= cur.age;
                cur.age += 1; // 양분 섭취 시에 나이 1 증가

                alive.add(cur); // 양분 섭취하고 살아있는 바이러스에 추가
            } else {
                dead.add(cur); // 양분 섭취 못하면 죽음
            }
        }

        virus.addAll(alive);
    }

    // 2. 죽은 바이러스가 양분으로 변함
    private static void changeNutrient() {
        while (!dead.isEmpty()) {
            Virus cur = dead.poll();
            map[cur.x][cur.y] += cur.age / 2; // 죽은 바이러스의 나이를 2로 나눈 값이 해당 칸의 양분으로 추가됨
        }
    }

    // 3. 바이러스 번식 진행
    private static void spreadVirus() {
        PriorityQueue<Virus> newVirus = new PriorityQueue<>();

        for (Virus cur : virus) {
            // 5의 배수의 나이를 가진 바이러스의 인접한 8개의 칸에만 진행
            if (cur.age % 5 == 0) {
                for (int d = 0; d < 8; d++) {
                    int nx = cur.x + dx[d];
                    int ny = cur.y + dy[d];

                    // 범위를 벗어나지 않는 경우
                    if (checkRange(nx, ny)) {
                        newVirus.add(new Virus(nx, ny, 1)); // 나이가 1인 바이러스 생김
                    }
                }
            }
        }

        virus.addAll(newVirus); // 새로 생긴 바이러스 원래 바이러스 큐에 추가
    }

    // 4. 각 칸에 양분 추가
    private static void addNutrient() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                map[i][j] += add[i][j];
            }
        }
    }

    private static boolean checkRange(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }
}
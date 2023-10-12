/**
 * 포탑 부수기
 * 삼성 SW 역량테스트 2023 상반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/destroy-the-turret/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 13. 수정
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Collections;

class Turret implements Comparable<Turret> {
	int x;
	int y;
	int power; // 공격력
	int attack; // 언제 공격했는지

	public Turret(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Turret(int x, int y, int power, int attack) {
		this.x = x;
		this.y = y;
		this.power = power;
		this.attack = attack;
	}

	@Override
	public int compareTo(Turret o) {
		/*
		 * 1. 공격력이 가장 낮은 포탑을 기준으로 오름차순 2. 공격력이 같다면 공격한 시점을 기준으로 내림차순 3. 공격한 시점도 같다면 행과
		 * 열의 합이 가장 큰 포탑을 기준으로 내림차순 4. 행과 열의 합도 같다면 열 값을 기준으로 내림차순
		 */

		if (this.power == o.power) {
			if (this.attack == o.attack) {
				if (this.x + this.y == o.x + o.y) {
					return o.y - this.y;
				}

				return (o.x + o.y) - (this.x + this.y);
			}

			return o.attack - this.attack;
		}

		return this.power - o.power;
	}
}

public class Main {

	// 우하좌상 -> 방향 우선순위
	static int[] dx = { 0, 1, 0, -1, -1, -1, 1, 1 };
	static int[] dy = { 1, 0, -1, 0, -1, 1, -1, 1 };

	static int N, M;
	static int[][] map; // 포탑 공격력 저장
	static int[][] attack; // 공격시점 저장
	static boolean[][] effect; // 영향 받았는지 확인 -> 공격 받았거나 공격자이거나 그 주변에 공격 받은 포탑인지 확인

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());

		map = new int[N][M];
		attack = new int[N][M];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());

			for (int j = 0; j < M; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		// K번의 턴이 종료된 수 남아있는 포탑 중 가장 강한 포탑의 공격력 출력
		for (int time = 1; time <= K; time++) {
			// 포탑이 하나만 있을 경우 종료
			if (isFisnish()) {
				break;
			}

			effect = new boolean[N][M]; // 매턴마다 초기화 해줘야함

			// 1. 공격력이 가장 낮고 높은 포탑 찾기
			ArrayList<Turret> list = new ArrayList<>();
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					if (map[i][j] > 0) {
						list.add(new Turret(i, j, map[i][j], attack[i][j]));
					}
				}
			}

			Collections.sort(list);

			Turret start = list.get(0); // 공격자 (가장 약한 포탑)
			Turret end = list.get(list.size() - 1); // 공격 대상 (가장 강한 포탑)

			// 공격력 증가
			map[start.x][start.y] += N + M;
			start.power = map[start.x][start.y];

			// 공격시점 저장
			attack[start.x][start.y] = time;
			start.attack = time;

			effect[start.x][start.y] = true;

			// 2. 포탑 공격(레이저공격 또는 포탄공격) -> 레이저공격을 하지 못하는 경우에 포탄공격
			if (!laser(start, end)) {
				bomb(start, end);
			}

			// 3. 포탑 부서짐 -> 공격 받아서 공격력이 0이하인 포탑은 0을 넣어줌
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					if (map[i][j] < 0) {
						map[i][j] = 0;
					}
				}
			}

			// 4. 포탑정비 -> 공격자가 아님, 공격에 피해입은 포탑 아님, 부서지지 않은 포탑이라면 공격력 1 증가
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					if (map[i][j] > 0 && !effect[i][j]) {
						map[i][j]++;
					}
				}
			}
		}

		// 남아있는 포탑 중 가장 강한 포탑의 공격력 출력
		int max = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				max = Math.max(max, map[i][j]);
			}
		}

		System.out.println(max);
	}

	// 레이저 공격 : bfs 사용 -> 공격자의 위치부터 시작 -> 공격 대상 위치까지 탐색 -> 만약 가는 길이 없다면 false 반환
	private static boolean laser(Turret start, Turret end) {
		Turret[][] route = new Turret[N][M]; // 경로 저장
		
		Queue<Turret> q = new LinkedList<>();
		boolean[][] visited = new boolean[N][M];

		q.add(new Turret(start.x, start.y));
		visited[start.x][start.y] = true;

		boolean isAttack = false;

		while (!q.isEmpty()) {
			Turret cur = q.poll();

			// 공격 위치에 도달한 경우 종료
			if (cur.x == end.x && cur.y == end.y) {
				isAttack = true;
				break;
			}

			for (int i = 0; i < 4; i++) {
				int nx = (N + cur.x + dx[i]) % N;
				int ny = (M + cur.y + dy[i]) % M;

				// 방문하지 않았고 부서지지 않은 포탑인 경우
				if (!visited[nx][ny] && map[nx][ny] > 0) {
					q.add(new Turret(nx, ny));
					visited[nx][ny] = true;
					route[nx][ny] = new Turret(cur.x, cur.y);
				}
			}
		}

		if (isAttack) {
			// 첫 번째 방법
			// // 역추적해서 경로에 있는 포탑 공격 -> 공격 대상 위치에서 시작하면됨(공격 대상 위치에 처음 도착하면 그게 최단경로)
			// int x = target.x;
			// int y = target.y;

			// while (!(x == start.x && y == start.y)) {
			// 	int power = start.power / 2;
				
			// 	if (x == target.x && y == target.y) {
			// 		power = start.power;
			// 	}

			// 	map[x][y] -= power;
			// 	effect[x][y] = true;

			// 	Turret turret = route[x][y]; // 역추적

			// 	x = turret.x;
			// 	y = turret.y;
			// }

			// 두 번째 방법
			// 공격대상 공격력 감소
			map[end.x][end.y] -= start.power;
			effect[end.x][end.y] = true;
			
			Turret turret = route[end.x][end.y];
			int x = turret.x;
			int y = turret.y;
			
			// 나머지 경로상에 있는 포탑은 (공격력 / 2)만큼 감소
			while (!(x == start.x && y == start.y)) {
				map[x][y] -= start.power / 2;
				effect[x][y] = true;
				
				turret = route[x][y];
				x = turret.x;
				y = turret.y;
			}
		}

		return isAttack;
	}

	// 포탄 공격 -> 매개변수 : 공격자, 공격 대상
	private static void bomb(Turret start, Turret end) {
		int power = start.power;

		map[end.x][end.y] -= power;
		effect[end.x][end.y] = true;

		power /= 2;

		// 공격 대상의 상하좌우, 대각선 위치의 포탄 공격력 감소
		for (int i = 0; i < 8; i++) {
			int nx = (N + end.x + dx[i]) % N;
			int ny = (M + end.y + dy[i]) % M;

			// 공격자의 위치가 아닌 경우 (공격자는 포탄 공격에 영향 받지 않음)
			if (!(nx == start.x && ny == start.y)) {
				map[nx][ny] -= power;
				effect[nx][ny] = true;
			}
		}
	}

	// 남아있는 포탑 개수 세기
	private static boolean isFisnish() {
		int cnt = 0;

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (map[i][j] > 0) {
					cnt++;
				}
			}
		}

		return cnt == 1;
	}

}
/**
 * 원자 충돌
 * 삼성 SW 역량테스트 2020 하반기 오전 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/atom-collision/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 8.
 * 
 * 백준 20056 마법사 상어와 파이어볼
 * https://www.acmicpc.net/problem/20056
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

class Atom {
	int x;
	int y;
	int m; // 질량
	int s; // 속력
	int d; // 방향 (0부터 7까지 순서대로 ↑, ↗, →, ↘, ↓, ↙, ←, ↖)
	
	public Atom(int x, int y, int m, int s, int d) {
		this.x = x;
		this.y = y;
		this.m = m;
		this.s = s;
		this.d = d;
	}
}

public class Main {
	
	static int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
	static int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};
	
	static int n, m, k;
	
	static ArrayList<Atom>[][] map; // 이동한 원자 정보 저장
	static ArrayList<Atom> atomList = new ArrayList<>(); // 원자 리스트

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			int m = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			
			atomList.add(new Atom(x, y, m, s, d));
		}
		
		// 맵 초기화
		map = new ArrayList[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				map[i][j] = new ArrayList<>();
			}
		}
		
		// k초만큼 진행
		while (k-- > 0) {
			moveAtom();
			synthesis();
		}
		
		// k초 이후 남아있는 원자들의 질량의 합을 출력
		int sum = 0;
		
		for (Atom atom : atomList) {
			sum += atom.m;
		}
		
		System.out.println(sum);

	}
	
	// 1. 원자 이동 -> 자신의 방향 d로 속력 s칸만큼 이동
	private static void moveAtom() {
		for (Atom atom : atomList) {
			atom.x = (n + atom.x + dx[atom.d] * (atom.s % n)) % n;
			atom.y = (n + atom.y + dy[atom.d] * (atom.s % n)) % n;
			
			map[atom.x][atom.y].add(atom); // 맵에 추가 -> 원자 합성하면서 맵을 clear해주기 때문에 바로 원자 추가함
		}
	}
	
	// 2. 하나의 칸에 2개 이상의 원자가 있는 경우 원자 합성
	private static void synthesis() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int cnt = map[i][j].size(); // 특정 칸의 원자 개수
				
				if (cnt >= 2) {
					int massSum = 0;
					int speedSum = 0;
					boolean odd = true; // 방향이 모두 홀수인지 저장
					boolean even = true; // 방향이 모두 짝수인지 확인
					
					// a. 같은 칸에 있는 원자 합치기 (질량, 속력 모두 합침)
					for (Atom atom : map[i][j]) {
						massSum += atom.m;
						speedSum += atom.s;
						
						if (atom.d % 2 == 0) {
							odd = false; // 하나라도 짝수이면 모두 홀수인지 확인하는 변수를 false로 변경
						} else {
							even = false; // 하나라도 홀수이면 모두 짝수인지 확인하는 변수를 false로 변경
						}
						
						atomList.remove(atom);
					}
					
					// b. 합쳐진 원자를 4개로 나눔
					int mass = massSum / 5; // 합쳐진 질량에 5를 나눔
					
					// 질량이 0인 원자는 소멸되기 때문에 0보다 클 경우에만 4개로 나눔
					if (mass > 0) {
						int speed = speedSum / cnt; // 합쳐진 속력에 합쳐진 원자의 개수 나눔
						
						// 모두 홀수이거나 짝수이면 각각 상하좌우의 값을 가지고(0, 2, 4, 6), 하나라도 다르면 각각 대각선의 값을 가짐(1, 3, 5, 7)
						int start = odd || even ? 0 : 1;
						
						for (int d = start; d < 8; d += 2) {
							atomList.add(new Atom(i, j, mass, speed, d));
						}
					}
				}
				
				map[i][j].clear(); // 원자가 합쳐지고 나눠진 결과는 atomList에 저장했기 때문에 clear 해줌
			}
		}
	}

}

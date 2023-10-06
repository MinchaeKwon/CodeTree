/**
 * 불안한 무빙워크
 * 삼성 SW 역량테스트 2020 하반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/unstable-moving-walk/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 7.
 * 
 * 백준 20055 컨베이어 벨트 위의 로봇
 * https://www.acmicpc.net/problem/20055
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	static int n, k;
	static int[] arr;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		n = Integer.parseInt(st.nextToken()); // 무빙워크 길이
		k = Integer.parseInt(st.nextToken()); // 실험 종료할 안정성이 0인 판의 개수
		
		arr = new int[n * 2];
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < n * 2; i++) {
			arr[i] = Integer.parseInt(st.nextToken()); // 무빙워크 칸의 안정성
		}
		
		conductExperiment();
	}
	
	// 실험 진행
	private static void conductExperiment() {
		int step = 0;
		
		boolean[] person = new boolean[n];
		
		// 실험이 종료될 때까지 진행
		while (!isEnd()) {
			// 사람이 어떤 칸에 올라가거나 이동하면 그 칸의 안정성은 즉시 1만큼 감소
			
			// 1. 무빙워크가 한 칸 회전함
			int last = arr[arr.length - 1]; // 마지막에 있는 내구도가 첫 번째 위치로 오기 때문에 미리 저장
			
			// 맨 뒤에부터 앞에 있는거 가져옴
			for (int i = n * 2 - 1; i > 0; i--) {
				arr[i] = arr[i - 1];
			}
			
			arr[0] = last;
			
			// 무빙워크가 회전하기 때문에 그 위에 있는 사람도 같이 움직임
			for (int i = n - 1; i > 0; i--) {
				person[i] = person[i - 1];
			}
			
			person[0] = false;
			person[n - 1] = false; // n번 칸에서 사람이 내리기 때문에 false
			
			// 2. 무빙워크에 올라가 있는 사람을 앞으로 한 칸 움직임 (앞선 칸에 사람이 이미 있거나 앞선 칸의 안정성이 0인 경우에는 이동하지 않음)
			for (int i = n - 1; i > 0; i--) {
				// 이동하는 칸에 사람이 있는 경우 && 이동시킬 칸에 사람이 없는 경우 && 안전성이 0보다 큰 경
				if (person[i - 1] && !person[i] && arr[i] > 0) {
					person[i - 1] = false;
					person[i] = true;
					arr[i]--; // 안정성 감소
				}
			}
			
			// 3. 1번 칸에 사람이 없고 안정성이 0이 아니라면 사람을 한 명 더 올림
			if (arr[0] > 0) { // 2번에서 사람을 앞으로 한 칸 이동시키므로 1번 칸에는 항상 사람이 없음 -> 안정성만 확인하면됨
				person[0] = true;
				arr[0]--; // 안정성 감소시킴
			}
			
			step++;
		}
		
		System.out.println(step);
	}
	
	// 실험이 종료되는지 확인 (안정성이 0인 칸이 k개 이상인지 확인)
	private static boolean isEnd() {
		int cnt = 0;
		
		for (int i = 0; i < n * 2; i++) {
			if (arr[i] == 0) {
				cnt++;
			}
		}
		
		return cnt >= k ? true : false;
	}

}

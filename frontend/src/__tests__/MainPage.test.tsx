import React from 'react';
import { RecoilRoot } from 'recoil';

import { MainPage } from '../pages';
import { loginState } from '../recoil';
import { render, screen, waitFor } from '../test-utils';

describe('메인 페이지 테스트', () => {
  beforeAll(() => {
    render(
      <RecoilRoot initializeState={(snap) => snap.set(loginState, true)}>
        <MainPage />
      </RecoilRoot>
    );
  });
  test('렌더링 테스트', async () => {
    await waitFor(() => {
      expect(screen.getByText('Java')).toBeVisible();
    });
  });
});

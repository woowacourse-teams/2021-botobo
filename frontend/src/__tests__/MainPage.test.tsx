import React from 'react';
import { RecoilRoot } from 'recoil';

import { MainPage } from '../pages';
import { userState } from '../recoil';
import { render, screen, waitFor } from '../test-utils';

describe('메인 페이지 테스트', () => {
  beforeAll(() => {
    render(
      <RecoilRoot
        initializeState={(snap) =>
          snap.set(userState, { userName: 'ditto', id: 1, profileUrl: '' })
        }
      >
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

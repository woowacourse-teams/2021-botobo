import styled from '@emotion/styled';
import React from 'react';

import PublicWorkbook from './PublicWorkbook';

const workbooks = [
  {
    id: 1,
    name: 'SpringSpringSpringSpringSpringSpringSpringSpringSpringSpring',
    cardCount: 157,
    author: '피케이',
  },
  {
    id: 2,
    name: 'Spring',
    cardCount: 33,
    author: '중간곰',
  },
  {
    id: 3,
    name: 'React',
    cardCount: 12,
    author: '카일',
  },
  {
    id: 4,
    name: 'JavaScript',
    cardCount: 4,
    author: '디토',
  },
  {
    id: 5,
    name: 'Network',
    cardCount: 387,
    author: '오즈',
  },
  {
    id: 6,
    name: 'CS',
    cardCount: 1,
    author: '조앤',
  },
];

interface Props {
  onClickPublicWorkbook: (id: number) => void;
}

// TODO: workbooks props로 받기(api 준비되면)
const PublicWorkbookList = ({ onClickPublicWorkbook }: Props) => {
  return (
    <StyledUl>
      {workbooks.map(({ id, name, cardCount, author }) => (
        <li key={id}>
          <PublicWorkbook
            name={name}
            cardCount={cardCount}
            author={author}
            onClick={() => onClickPublicWorkbook(id)}
          />
        </li>
      ))}
    </StyledUl>
  );
};

const StyledUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(1);
  gap: 1rem;
`;

export default PublicWorkbookList;

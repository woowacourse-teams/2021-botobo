import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';
import { useSetRecoilState } from 'recoil';

import EmptyStarIcon from '../assets/star-empty.svg';
import FillStarIcon from '../assets/star-fill.svg';
import { STORAGE_KEY } from '../constants';
import { useRouter } from '../hooks';
import { cardIdState } from '../recoil';
import { CardResponse } from '../types';
import { debounce, setSessionStorage } from '../utils';
import CardTemplate from './CardTemplate';

interface Props {
  cardInfo: CardResponse;
  deleteCard: (id: number) => Promise<void>;
  toggleBookmark: (cardInfo: CardResponse) => Promise<void>;
}

const QnACard = ({ cardInfo, deleteCard, toggleBookmark }: Props) => {
  const { routeCardEdit } = useRouter();
  const setCardId = useSetRecoilState(cardIdState);
  const [isBookmark, setIsBookmark] = useState(false);

  const onClickBookmark = () => {
    setIsBookmark((prevState) => !prevState);
    debounce(() => toggleBookmark({ ...cardInfo, bookmark: isBookmark }), 500);
  };

  return (
    <CardTemplate
      editable={true}
      onClickEditButton={async () => {
        await setCardId(cardInfo.id);
        setSessionStorage(STORAGE_KEY.CARD_ID, cardInfo.id);
        routeCardEdit();
      }}
      onClickDeleteButton={() => deleteCard(cardInfo.id)}
    >
      <Header>
        <BookmarkButton onClick={onClickBookmark}>
          {isBookmark ? <FillStarIcon /> : <EmptyStarIcon />}
        </BookmarkButton>
      </Header>
      <Question>Q. {cardInfo.question}</Question>
      <Answer>A. {cardInfo.answer}</Answer>
    </CardTemplate>
  );
};

const Header = styled.div`
  text-align: right;
  height: 1.5rem;
  margin-bottom: 1rem;
`;

const BookmarkButton = styled.button`
  & > svg {
    height: 1.5rem;
    width: 1.5rem;
  }
`;

const Question = styled.div`
  padding-bottom: 1rem;

  ${({ theme }) => css`
    border-bottom: 2px solid ${theme.color.gray_3};
  `}
`;

const Answer = styled.div`
  padding-top: 1.5rem;
`;

export default QnACard;

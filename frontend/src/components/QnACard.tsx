import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';
import { useSetRecoilState } from 'recoil';

import EmptyStarIcon from '../assets/star-empty.svg';
import FillStarIcon from '../assets/star-fill.svg';
import useModal from '../hooks/useModal';
import { cardIdState } from '../recoil';
import { CardResponse } from '../types';
import { debounce } from '../utils';
import CardEditForm from './CardEditForm';
import CardTemplate from './CardTemplate';

interface Props {
  cardInfo: CardResponse;
  workbookName: string;
  editCard: (cardInfo: CardResponse) => Promise<void>;
  deleteCard: (id: number) => Promise<void>;
  toggleBookmark: (cardInfo: CardResponse) => Promise<void>;
}

const QnACard = ({
  cardInfo,
  workbookName,
  editCard,
  deleteCard,
  toggleBookmark,
}: Props) => {
  const { openModal } = useModal();
  const setCardId = useSetRecoilState(cardIdState);
  const [isBookmark, setIsBookmark] = useState(cardInfo.bookmark);

  const onClickBookmark = () => {
    setIsBookmark((prevState) => !prevState);
    debounce(() => toggleBookmark({ ...cardInfo, bookmark: !isBookmark }), 500);
  };

  return (
    <CardTemplate
      editable={true}
      onClickEditButton={async () => {
        await setCardId(cardInfo.id);
        openModal({
          content: <CardEditForm cardInfo={cardInfo} onSubmit={editCard} />,
          title: workbookName,
          type: 'full',
        });
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
